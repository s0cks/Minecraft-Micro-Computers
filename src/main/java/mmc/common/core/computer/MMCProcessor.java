package mmc.common.core.computer;

import io.github.s0cks.mmc.Address;
import io.github.s0cks.mmc.Binary;
import io.github.s0cks.mmc.Instruction;
import io.github.s0cks.mmc.Operand;
import io.github.s0cks.mmc.OperandAddress;
import io.github.s0cks.mmc.OperandInteger;
import io.github.s0cks.mmc.OperandRegister;
import io.github.s0cks.mmc.Register;
import io.github.s0cks.mmc.util.InstructionDecoder;
import mmc.api.computer.IProcessor;
import mmc.api.computer.ITerminal;

import java.util.Arrays;

public final class MMCProcessor
implements IProcessor {
  public final ITerminal terminal;
  public final short[][] registers = new short[Register.values().length][2];
  public final int[] memory = new int[65535];
  private short end = 0;
  private short pc = 0;
  private short retPC = 0;
  private int counter = 0;
  private int writeOffset = 0;

  public MMCProcessor(ITerminal terminal) {
    this.terminal = terminal;
  }

  @Override
  public void setMemory(int[] mem) {
    System.arraycopy(mem, 0, this.memory, 0, this.memory.length);
  }

  @Override
  public void loadBinary(Binary binary) {
    this.end = (short) binary.counter();
    for (int i = 0; i < 65535; i++) {
      if(i < binary.counter()){
        this.memory[i] = binary.get(i);
      } else{
        this.memory[i] = 0x0;
      }
    }
  }

  private Operand<?> getOperand(short value) {
    if (value < Register.values().length) {
      return new OperandRegister(Register.values()[value & 0x7]);
    } else if (value < 0x10 + Register.values().length) {
      Register base = Register.values()[value & 0x7];
      int offset = ((int) this.memory[this.pc]);
      this.pc += 1;
      return new OperandAddress(base, offset);
    } else if (value >= 0x20) {
      return new OperandInteger(value - 0x20);
    } else {
      if (value == 0x1F) {
        OperandInteger ret = new OperandInteger(this.memory[this.pc]);
        this.pc += 1;
        return ret;
      }
      return new OperandInteger(0);
    }
  }

  @Override
  public int registerValue(Register reg) {
    return (
           this.registers[reg.ordinal()][0] |
           (this.registers[reg.ordinal()][1] << 8)
    );
  }

  @Override
  public int[] memory() {
    return Arrays.copyOf(this.memory, this.memory.length);
  }

  @Override
  public int memoryValue(Address addr) {
    return (
           this.memory[this.registerValue(addr.register) + addr.offset] |
           (this.memory[this.registerValue(addr.register) + addr.offset + 1] << 8)
    );
  }

  @Override
  public int memoryValue(int address) {
    return (
           this.memory[address] |
           (this.memory[address + 1] << 8)
    );
  }

  private void mov(Register dest, Register src) {
    this.registers[dest.ordinal()][0] = this.registers[src.ordinal()][0];
    this.registers[dest.ordinal()][1] = this.registers[src.ordinal()][1];
  }

  private void mov(Register reg, Address destAddr) {
    this.mov(destAddr, (this.registers[reg.ordinal()][0] | (this.registers[reg.ordinal()][1] << 8)));
  }

  private void mov(Address addr, Register src) {
    this.mov(addr, this.registerValue(src));
  }

  private void mov(Register reg, int value) {
    this.registers[reg.ordinal()][0] = ((short) ((value & 0xFF)));
    this.registers[reg.ordinal()][1] = ((short) ((value >> 8) & 0xFF));
  }

  private void mov(Address addr, int value) {
    this.memory[this.registerValue(addr.register) + addr.offset] = ((short) (value & 0xFF));
    this.memory[this.registerValue(addr.register) + addr.offset + 1] = ((short) ((value >> 8) & 0xFF));
  }

  private void mov(Operand<?> dest, Operand<?> loc) {
    switch (dest.type()) {
      case ADDRESS: {
        OperandAddress destAddr = ((OperandAddress) dest);
        switch (loc.type()) {
          case ADDRESS: {
            OperandAddress locAddr = ((OperandAddress) loc);
            this.mov(Register.RCX, locAddr.address);
            this.mov(destAddr.address, Register.RCX);
            break;
          }
          case REGISTER: {
            this.mov(((OperandRegister) loc).value(), destAddr.address);
            break;
          }
          case LITERAL: {
            this.mov(destAddr.address, ((OperandInteger) loc).value());
            break;
          }
        }
        break;
      }
      case REGISTER: {
        Register destReg = ((OperandRegister) dest).value();
        switch (loc.type()) {
          case ADDRESS: {
            this.mov(destReg, ((OperandAddress) loc).value());
            break;
          }
          case LITERAL: {
            this.mov(destReg, ((OperandInteger) loc).value());
            break;
          }
          case REGISTER: {
            this.mov(destReg, ((OperandRegister) loc).value());
            break;
          }
        }
      }
    }
  }

  private void jmp(Operand<?> dest) {
    switch (dest.type()) {
      case ADDRESS: {
        Register destReg = ((OperandAddress) dest).address.register;
        int offset = ((OperandAddress) dest).address.offset;
        this.pc = (short) this.memory[this.registers[destReg.ordinal()][offset]];
        break;
      }
      case REGISTER: {
        Register destReg = ((OperandRegister) dest).value();
        this.pc = (short) ((this.registers[destReg.ordinal()][1] & 0xFF) | (this.registers[destReg.ordinal()][1]));
        break;
      }
      case LITERAL: {
        this.pc = ((OperandInteger) dest).value()
                                         .shortValue();
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
        System.out.println(value);
        switch (value) {
          case 0xA: {
            this.mov(Register.RCX, 0x8000);
            this.mov(new Address(Register.RCX, this.writeOffset), Register.RDI);
            this.writeOffset += 2;
            break;
          }
        }
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
      short op = InstructionDecoder.decodeInstruction((short) this.memory[this.pc]);
      short opA = InstructionDecoder.decodeOperandA((short) this.memory[this.pc]);
      short opB = InstructionDecoder.decodeOperandB((short) this.memory[this.pc]);
      this.pc++;

      if (op >= 0) {
        if (InstructionDecoder.isJmp((short) this.memory[this.pc - 1])) {
          this.step(Instruction.JMP, opA, opB);
        } else if (InstructionDecoder.isSysCall((short) this.memory[this.pc - 1])) {
          this.step(Instruction.SYSCALL, opA, opB);
        } else {
          this.step(Instruction.values()[op], opA, opB);
        }
      }
    } catch(Exception e){
      // Fallthrough
    }
  }
}
