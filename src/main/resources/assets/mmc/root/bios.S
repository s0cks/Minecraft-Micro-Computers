jmp _main

_bios_version_s:
    db "Asyncronous"

_space_s:
    db " "

_arrow_s:
    db ">"

_print_bios:
    mov RAX, 12
    mov RDI, _bios_version_s
    syscall 15
    mov RAX, 2
    mov RDI, _space_s
    syscall 15
    mov RAX, 2
    mov RDI, _arrow_s
    syscall 15
    ret

_main:
    call _print_bios