; System Calls:
;   + 10 -> Print Char e.x:
;       mov RDI, 70 ; mov F to RDI
;       syscall 10
;
;   + 11 -> Clear Display e.x:
;       syscall 11
;
;   + 12 -> Set Cursor Pos e.x:
;       mov RDI, 1 ; mov 1 into first arg register
;       mov RAX, 1 ; mov 1 into second arg register
;       syscall 12 ; move cursor to 1,1
;
_print_f:
    mov RDI, 70
    syscall 10
    ret

call _print_f