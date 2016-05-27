package mmc.common.core.computer;

import io.github.s0cks.mmc.Address;
import io.github.s0cks.mmc.Binary;
import io.github.s0cks.mmc.Instruction;
import io.github.s0cks.mmc.Operand;
import io.github.s0cks.mmc.OperandAddress;
import io.github.s0cks.mmc.OperandBytes;
import io.github.s0cks.mmc.OperandInteger;
import io.github.s0cks.mmc.OperandLabel;
import io.github.s0cks.mmc.OperandRegister;
import io.github.s0cks.mmc.Register;
import io.github.s0cks.mmc.util.InstructionDecoder;
import mmc.api.computer.IProcessor;
import mmc.api.computer.ITerminal;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public final class MMCProcessor
implements IProcessor {
  private static final int[] blankPage = new int[0x100];
  static{
    Arrays.fill(blankPage, 0x0);
  }

  private static final Map<Integer, Function<MMCProcessor, Void>> syscalls = new HashMap<>();

  static{
    syscalls.put(0xB, (processor -> {
      return null;
    }));

    syscalls.put(0xC, (processor -> {
      return null;
    }));

    syscalls.put(0xA, (processor -> {
      return null;
    }));

    syscalls.put(0xF, (processor -> {
      processor.mov(new OperandRegister(Register.RCX), new OperandInteger(0x8000));
      for(int i = 0; i < (processor.registerValue(Register.RAX) * 2) - 2; i += 2){
        processor.mov(new OperandAddress(Register.RCX, processor.writeOffset), new OperandAddress(Register.RDI, i + 2));
        processor.writeOffset += 2;
      }
      return null;
    }));
  }

  public final ITerminal terminal;
  public final int pageMask = 0xFFFFFFFF;
  public final int pageCount = (0x10000 - 1) / 0x100 + 1;
  public final int[] registers = new int[Register.values().length];
  public final int[][] pages = new int[this.pageCount][];
  private int end = 0;
  private int retPC = 0;
  private int pc = 0;
  private int writeOffset = 0;

  public MMCProcessor(ITerminal terminal) {
    this.terminal = terminal;
  }

  @Override
  public void setMemory(int[] mem) {
    int pageCount = mem.length / 0x100;
    if(pageCount > this.pageCount) throw new IllegalStateException("Memory overflow");

    for(int i = 0; i < pageCount; i++){
      for(int j = 0; j < 0x100; j++){
        int[] page = new int[0x100];
        System.arraycopy(mem, i * 0x100 + j, page, 0, 0x100);
        this.pages[i] = page;
      }
    }
  }

  public void writeMemory(int loc, int value){
    int page = loc / 0x100;
    if(page >= this.pageCount) throw new IllegalStateException("Attempt to write paste end of memory");
    if(this.pages[page] == null) this.pages[page] = new int[0x100];
    this.pages[page][loc % 0x100] = value & this.pageMask;
  }

  @Override
  public void loadBinary(Binary binary) {
    this.end = (short) binary.counter();
    for (int i = 0; i < 65535; i++) {
      if(i < this.end){
        this.writeMemory(i, binary.get(i));
      } else{
        this.writeMemory(i, 0x0);
      }
    }
  }

  private Operand<?> getOperand(short value) {
    if(value < Register.values().length){
      return new OperandRegister(Register.values()[value & 0x7]);
    } else if(value == 0x3F){
      int len = this.memoryValue(this.pc);
      String str = "";
      for(int i = this.pc; i < this.pc + len; i += 2){
        int high = this.memoryValue(i);
        int low = this.memoryValue(i + 1);
        str += ((char) high | (low << 8));
      }
      this.pc += len + 1;
      return new OperandBytes(str);
    } else if(value < 0x10 + Register.values().length){
      Register reg = Register.values()[value & 0x7];
      int offset = this.memoryValue(this.pc);
      return new OperandAddress(reg, offset);
    } else if(value == 0x2F){
      int label = this.memoryValue(this.pc += 1);
      return new OperandLabel(label);
    } else if(value == 0x1F){
      int literal = this.memoryValue(this.pc += 1);
      return new OperandInteger(literal);
    }

    this.pc += 1;
    return null;
  }

  @Override
  public int registerValue(Register reg) {
    return this.registers[reg.ordinal()] & 0xFFFF;
  }

  public void setRegisterValue(Register reg, int value){
    this.registers[reg.ordinal()] = value & 0xFFFF;
  }

  @Override
  public int[] memory() {
    int size = this.pageCount * 0x100;
    int[] memory = new int[size];
    for(int i = 0; i < this.pages.length; i++){
      int[] page = this.pages[i];
      if(page != null){
        System.arraycopy(page, 0, memory, i * 0x100, 0x100);
      } else{
        System.arraycopy(blankPage, 0, memory, i * 0x100, 0x100);
      }
    }
    return memory;
  }

  @Override
  public int memoryValue(Address addr) {
    return this.memoryValue(this.registerValue(addr.register) + addr.offset);
  }

  @Override
  public int memoryValue(int address) {
    int page = address / 0x100;
    if(page >= this.pageCount) throw new IllegalStateException("Attempt to read past end of memory");
    if(this.pages[page] == null) return 0;
    return this.pages[page][address % 0x100] & this.pageMask;
  }

  private void jmp(Operand<?> dest){
    switch(dest.type()){
      case ADDRESS:{
        Address destAddr = ((OperandAddress) dest).address;
        this.pc = this.memoryValue(destAddr) - 1;
        break;
      }
      case LABEL:{
        this.pc = ((OperandLabel) dest).value() - 1;
        break;
      }
      default: throw new IllegalStateException("Illegal Address Mode For JMP: " + dest.toString());
    }
  }

  private void mov(Operand<?> dest, Operand<?> loc){
    switch(dest.type()){
      case REGISTER:{
        Register destReg = ((OperandRegister) dest).value();
        switch(loc.type()){
          case REGISTER:{
            Register locReg = ((OperandRegister) loc).value();
            this.setRegisterValue(destReg, this.registerValue(locReg));
            break;
          }
          case ADDRESS:{
            Address locAddr = ((OperandAddress) loc).value();
            this.setRegisterValue(destReg, this.memoryValue(locAddr));
            break;
          }
          case LITERAL:{
            int locLit = ((OperandInteger) loc).value();
            this.setRegisterValue(destReg, locLit);
            break;
          }
          case LABEL:{
            int label = ((OperandLabel) loc).value();
            this.setRegisterValue(destReg, label);
            break;
          }
          default: throw new IllegalStateException("Invalid address mode: " + loc.type() + " -> " + dest.type());
        }
        break;
      }
      case ADDRESS:{
        Address destAddr = ((OperandAddress) dest).value();
        switch(loc.type()){
          case ADDRESS:{
            Address locAddr = ((OperandAddress) loc).value();
            this.writeMemory(this.registerValue(destAddr.register) + destAddr.offset, this.memoryValue(locAddr));
            break;
          }
          case REGISTER:{
            Register locReg = ((OperandRegister) loc).value();
            this.writeMemory(this.registerValue(destAddr.register) + destAddr.offset, this.registerValue(locReg));
            break;
          }
          case LITERAL:{
            int locLit = ((OperandInteger) loc).value();
            this.writeMemory(this.registerValue(destAddr.register) + destAddr.offset, locLit);
            break;
          }
          case BYTES:{
            short[] bytes = ((OperandBytes) loc).value();
            for(int i = 0; i < bytes.length; i += 2){
              this.writeMemory(this.registerValue(destAddr.register) + destAddr.offset + i, bytes[i] | (bytes[i] << 8));
            }
            break;
          }
          case LABEL:{
            int label = ((OperandLabel) loc).value();
            this.writeMemory(this.registerValue(destAddr.register) + destAddr.offset, label);
            break;
          }
          default: throw new IllegalStateException("Invalid address mode: " + loc.type() + " -> " + dest.type());
        }
        break;
      }
    }
  }

  private void step(Instruction instr, short opA, short opB) {
    System.out.println(instr);
    switch (instr) {
      case MOV: {
        this.mov(this.getOperand(opA), this.getOperand(opB));
        break;
      }
      case RET: {
        this.pc = this.retPC;
        break;
      }
      case JMP: {
        this.jmp(this.getOperand(opB));
        break;
      }
      case SYSCALL: {
        int value = ((OperandInteger) this.getOperand(opB)).value();
        syscalls.get(value).apply(this);
        break;
      }
      case CALL: {
        this.retPC = this.pc;
        this.jmp(this.getOperand(opB));
        break;
      }
      default: break;
    }
  }

  @Override
  public void tick() {
    try{
      if(this.pc > this.end) return;
      short op = InstructionDecoder.decodeInstruction(((short) this.memoryValue(this.pc)));
      short opA = InstructionDecoder.decodeOperandA(((short) this.memoryValue(this.pc)));
      short opB = InstructionDecoder.decodeOperandB(((short) this.memoryValue(this.pc)));

      if (op > 0) {
        if (InstructionDecoder.isJmp(((short) this.memoryValue(this.pc)))) {
          this.step(Instruction.JMP, opA, opB);
        } else if (InstructionDecoder.isSysCall(((short) this.memoryValue(this.pc)))) {
          this.step(Instruction.SYSCALL, opA, opB);
        } else {
          this.step(Instruction.values()[op], opA, opB);
        }
      }

    } catch(Exception e){
      throw new RuntimeException(e);
    }
    this.pc++;
  }
}
