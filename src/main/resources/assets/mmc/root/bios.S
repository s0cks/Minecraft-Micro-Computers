jmp _main

_bios_header_s:
    db "Hello World"

_main:
    mov RDI, _bios_header_s
    mov RAX, 11
    call _print