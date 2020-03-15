.data
LL$$:
    .quad 0 # No super class
    .quad LL$Start$
Element$$:
    .quad 0 # No super class
    .quad Element$Compare$
    .quad Element$Equal$
    .quad Element$GetAge$
    .quad Element$GetMarried$
    .quad Element$GetSalary$
    .quad Element$Init$
List$$:
    .quad 0 # No super class
    .quad List$Delete$
    .quad List$GetElem$
    .quad List$GetEnd$
    .quad List$GetNext$
    .quad List$Init$
    .quad List$InitNew$
    .quad List$Insert$
    .quad List$Print$
    .quad List$Search$
    .quad List$SetNext$
LinkedList$$:
    .quad 0 # No super class
.global asm_main
    
asm_main:
    # Prologue start
    pushq %rbp
    movq %rsp, %rbp
    subq $0, %rsp
    # Prologue end
    # Print -- Start line: 3
    # Call -- Start line: 3
    pushq %rdi
    # New Object -- Start line: 3
    pushq %rdi
    movq $8 , %rdi
    call mjcalloc
    leaq LL$$, %rdx
    movq %rdx, 0(%rax)
    popq %rdi
    # New Object -- end line: 3
    movq %rax, %rdi
    pushq %rax
    popq %rax
    movq %rax, %rdi
    movq (%rax), %r10
    call *8(%r10)
    popq %rdi
    # Call -- End line: 3
    pushq %rdi
    movq %rax, %rdi
    call put
    popq %rdi
    # Print -- End line: 3
    # Epilogue start
    movq %rbp, %rsp
    popq %rbp
    ret
    # Epilogue end
Element$Init$:
    # Prologue start
    pushq %rbp
    movq %rsp, %rbp
    subq $0, %rsp
    # Prologue end
    # Assignment -- Start line: 14
    # IdentifierExp -- Start line: 14
    movq %rsi ,  %rax
    # IdentifierExp -- End line: 14
    movq %rax ,     8(%rdi)
    # Assignment -- End line: 14
    # Assignment -- Start line: 15
    # IdentifierExp -- Start line: 15
    movq %rdx ,  %rax
    # IdentifierExp -- End line: 15
    movq %rax ,     16(%rdi)
    # Assignment -- End line: 15
    # Assignment -- Start line: 16
    # IdentifierExp -- Start line: 16
    movq %rcx ,  %rax
    # IdentifierExp -- End line: 16
    movq %rax ,     24(%rdi)
    # Assignment -- End line: 16
    movq $1, %rax
    # Epilogue start
    movq %rbp, %rsp
    popq %rbp
    ret
    # Epilogue end
Element$GetAge$:
    # Prologue start
    pushq %rbp
    movq %rsp, %rbp
    subq $0, %rsp
    # Prologue end
    # IdentifierExp -- Start line: 21
    movq 8(%rdi) ,  %rax
    # IdentifierExp -- End line: 21
    # Epilogue start
    movq %rbp, %rsp
    popq %rbp
    ret
    # Epilogue end
Element$GetSalary$:
    # Prologue start
    pushq %rbp
    movq %rsp, %rbp
    subq $0, %rsp
    # Prologue end
    # IdentifierExp -- Start line: 25
    movq 16(%rdi) ,  %rax
    # IdentifierExp -- End line: 25
    # Epilogue start
    movq %rbp, %rsp
    popq %rbp
    ret
    # Epilogue end
Element$GetMarried$:
    # Prologue start
    pushq %rbp
    movq %rsp, %rbp
    subq $0, %rsp
    # Prologue end
    # IdentifierExp -- Start line: 29
    movq 24(%rdi) ,  %rax
    # IdentifierExp -- End line: 29
    # Epilogue start
    movq %rbp, %rsp
    popq %rbp
    ret
    # Epilogue end
Element$Equal$:
    # Prologue start
    pushq %rbp
    movq %rsp, %rbp
    subq $32, %rsp
    # Prologue end
    # Assignment -- Start line: 39
    movq $1, %rax
    movq %rax, -8(%rbp)
    # Assignment -- End line: 39
    # Assignment -- Start line: 41
    # Call -- Start line: 41
    pushq %rdi
    pushq %rsi
    # IdentifierExp -- Start line: 41
    movq %rsi ,  %rax
    # IdentifierExp -- End line: 41
    movq %rax, %rdi
    pushq %rax
    popq %rax
    movq %rax, %rdi
    movq (%rax), %r10
    call *24(%r10)
    popq %rsi
    popq %rdi
    # Call -- End line: 41
    movq %rax, -16(%rbp)
    # Assignment -- End line: 41
    # Call -- Start line: 42
    pushq %rdi
    pushq %rsi
    movq %rdi, %rax
    movq %rax, %rdi
    pushq %rax
    # IdentifierExp -- Start line: 42
    movq -16(%rbp), %rax
    # IdentifierExp -- End line: 42
    movq %rax, %rsi
    # IdentifierExp -- Start line: 42
    movq 8(%rdi) ,  %rax
    # IdentifierExp -- End line: 42
    movq %rax, %rdx
    popq %rax
    movq %rax, %rdi
    movq (%rax), %r10
    call *8(%r10)
    popq %rsi
    popq %rdi
    # Call -- End line: 42
    # Not -- Start line: 42
    pushq %r10
    movq $1, %r10
    subq %rax, %r10
    movq %r10, %rax
    popq %r10
    # Not -- End line: 42
    cmpq $1, %rax
    jne FALSE0
TRUE0:
    # Assignment -- Start line: 42
    movq $0, %rax
    movq %rax, -8(%rbp)
    # Assignment -- End line: 42
    jmp CONTINUE0
FALSE0:
    # Assignment -- Start line: 44
    # Call -- Start line: 44
    pushq %rdi
    pushq %rsi
    # IdentifierExp -- Start line: 44
    movq %rsi ,  %rax
    # IdentifierExp -- End line: 44
    movq %rax, %rdi
    pushq %rax
    popq %rax
    movq %rax, %rdi
    movq (%rax), %r10
    call *40(%r10)
    popq %rsi
    popq %rdi
    # Call -- End line: 44
    movq %rax, -24(%rbp)
    # Assignment -- End line: 44
    # Call -- Start line: 45
    pushq %rdi
    pushq %rsi
    movq %rdi, %rax
    movq %rax, %rdi
    pushq %rax
    # IdentifierExp -- Start line: 45
    movq -24(%rbp), %rax
    # IdentifierExp -- End line: 45
    movq %rax, %rsi
    # IdentifierExp -- Start line: 45
    movq 16(%rdi) ,  %rax
    # IdentifierExp -- End line: 45
    movq %rax, %rdx
    popq %rax
    movq %rax, %rdi
    movq (%rax), %r10
    call *8(%r10)
    popq %rsi
    popq %rdi
    # Call -- End line: 45
    # Not -- Start line: 45
    pushq %r10
    movq $1, %r10
    subq %rax, %r10
    movq %r10, %rax
    popq %r10
    # Not -- End line: 45
    cmpq $1, %rax
    jne FALSE1
TRUE1:
    # Assignment -- Start line: 45
    movq $0, %rax
    movq %rax, -8(%rbp)
    # Assignment -- End line: 45
    jmp CONTINUE1
FALSE1:
    # IdentifierExp -- Start line: 47
    movq 24(%rdi) ,  %rax
    # IdentifierExp -- End line: 47
    cmpq $1, %rax
    jne FALSE2
TRUE2:
    # Call -- Start line: 48
    pushq %rdi
    pushq %rsi
    # IdentifierExp -- Start line: 48
    movq %rsi ,  %rax
    # IdentifierExp -- End line: 48
    movq %rax, %rdi
    pushq %rax
    popq %rax
    movq %rax, %rdi
    movq (%rax), %r10
    call *32(%r10)
    popq %rsi
    popq %rdi
    # Call -- End line: 48
    # Not -- Start line: 48
    pushq %r10
    movq $1, %r10
    subq %rax, %r10
    movq %r10, %rax
    popq %r10
    # Not -- End line: 48
    cmpq $1, %rax
    jne FALSE3
TRUE3:
    # Assignment -- Start line: 48
    movq $0, %rax
    movq %rax, -8(%rbp)
    # Assignment -- End line: 48
    jmp CONTINUE3
FALSE3:
    # Assignment -- Start line: 49
    movq $0, %rax
    movq %rax, -32(%rbp)
    # Assignment -- End line: 49
    jmp CONTINUE3
CONTINUE3:
    jmp CONTINUE2
FALSE2:
    # Call -- Start line: 51
    pushq %rdi
    pushq %rsi
    # IdentifierExp -- Start line: 51
    movq %rsi ,  %rax
    # IdentifierExp -- End line: 51
    movq %rax, %rdi
    pushq %rax
    popq %rax
    movq %rax, %rdi
    movq (%rax), %r10
    call *32(%r10)
    popq %rsi
    popq %rdi
    # Call -- End line: 51
    cmpq $1, %rax
    jne FALSE4
TRUE4:
    # Assignment -- Start line: 51
    movq $0, %rax
    movq %rax, -8(%rbp)
    # Assignment -- End line: 51
    jmp CONTINUE4
FALSE4:
    # Assignment -- Start line: 52
    movq $0, %rax
    movq %rax, -32(%rbp)
    # Assignment -- End line: 52
    jmp CONTINUE4
CONTINUE4:
    jmp CONTINUE2
CONTINUE2:
    jmp CONTINUE1
CONTINUE1:
    jmp CONTINUE0
CONTINUE0:
    # IdentifierExp -- Start line: 55
    movq -8(%rbp), %rax
    # IdentifierExp -- End line: 55
    # Epilogue start
    movq %rbp, %rsp
    popq %rbp
    ret
    # Epilogue end
Element$Compare$:
    # Prologue start
    pushq %rbp
    movq %rsp, %rbp
    subq $16, %rsp
    # Prologue end
    # Assignment -- Start line: 64
    movq $0, %rax
    movq %rax, -8(%rbp)
    # Assignment -- End line: 64
    # Assignment -- Start line: 65
    # Plus -- Start line: 65
    # IdentifierExp -- Start line: 65
    movq %rdx ,  %rax
    # IdentifierExp -- End line: 65
    pushq %rax
    movq $1, %rax
    popq %rbx
    addq %rbx, %rax
    # Plus -- End line: 65
    movq %rax, -16(%rbp)
    # Assignment -- End line: 65
    # IdentifierExp -- Start line: 66
    movq %rsi ,  %rax
    # IdentifierExp -- End line: 66
    movq %rax, %r10
    # IdentifierExp -- Start line: 66
    movq %rdx ,  %rax
    # IdentifierExp -- End line: 66
    movq %rax, %r11
    cmpq %r11, %r10
    jl TRUE6
    jge FALSE6
TRUE6:
    movq $1, %rax
    jmp CONTINUE6
FALSE6:
    movq $0, %rax
    jmp CONTINUE6
CONTINUE6:
    cmpq $1, %rax
    jne FALSE5
TRUE5:
    # Assignment -- Start line: 66
    movq $0, %rax
    movq %rax, -8(%rbp)
    # Assignment -- End line: 66
    jmp CONTINUE5
FALSE5:
    # IdentifierExp -- Start line: 67
    movq %rsi ,  %rax
    # IdentifierExp -- End line: 67
    movq %rax, %r10
    # IdentifierExp -- Start line: 67
    movq -16(%rbp), %rax
    # IdentifierExp -- End line: 67
    movq %rax, %r11
    cmpq %r11, %r10
    jl TRUE8
    jge FALSE8
TRUE8:
    movq $1, %rax
    jmp CONTINUE8
FALSE8:
    movq $0, %rax
    jmp CONTINUE8
CONTINUE8:
    # Not -- Start line: 67
    pushq %r10
    movq $1, %r10
    subq %rax, %r10
    movq %r10, %rax
    popq %r10
    # Not -- End line: 67
    cmpq $1, %rax
    jne FALSE7
TRUE7:
    # Assignment -- Start line: 67
    movq $0, %rax
    movq %rax, -8(%rbp)
    # Assignment -- End line: 67
    jmp CONTINUE7
FALSE7:
    # Assignment -- Start line: 68
    movq $1, %rax
    movq %rax, -8(%rbp)
    # Assignment -- End line: 68
    jmp CONTINUE7
CONTINUE7:
    jmp CONTINUE5
CONTINUE5:
    # IdentifierExp -- Start line: 69
    movq -8(%rbp), %rax
    # IdentifierExp -- End line: 69
    # Epilogue start
    movq %rbp, %rsp
    popq %rbp
    ret
    # Epilogue end
List$Init$:
    # Prologue start
    pushq %rbp
    movq %rsp, %rbp
    subq $0, %rsp
    # Prologue end
    # Assignment -- Start line: 81
    movq $1, %rax
    movq %rax ,     24(%rdi)
    # Assignment -- End line: 81
    movq $1, %rax
    # Epilogue start
    movq %rbp, %rsp
    popq %rbp
    ret
    # Epilogue end
List$InitNew$:
    # Prologue start
    pushq %rbp
    movq %rsp, %rbp
    subq $0, %rsp
    # Prologue end
    # Assignment -- Start line: 87
    # IdentifierExp -- Start line: 87
    movq %rcx ,  %rax
    # IdentifierExp -- End line: 87
    movq %rax ,     24(%rdi)
    # Assignment -- End line: 87
    # Assignment -- Start line: 88
    # IdentifierExp -- Start line: 88
    movq %rsi ,  %rax
    # IdentifierExp -- End line: 88
    movq %rax ,     8(%rdi)
    # Assignment -- End line: 88
    # Assignment -- Start line: 89
    # IdentifierExp -- Start line: 89
    movq %rdx ,  %rax
    # IdentifierExp -- End line: 89
    movq %rax ,     16(%rdi)
    # Assignment -- End line: 89
    movq $1, %rax
    # Epilogue start
    movq %rbp, %rsp
    popq %rbp
    ret
    # Epilogue end
List$Insert$:
    # Prologue start
    pushq %rbp
    movq %rsp, %rbp
    subq $24, %rsp
    # Prologue end
    # Assignment -- Start line: 98
    movq %rdi, %rax
    movq %rax, -16(%rbp)
    # Assignment -- End line: 98
    # Assignment -- Start line: 99
    # New Object -- Start line: 99
    pushq %rdi
    pushq %rsi
    movq $32 , %rdi
    call mjcalloc
    leaq List$$, %rdx
    movq %rdx, 0(%rax)
    popq %rsi
    popq %rdi
    # New Object -- end line: 99
    movq %rax, -24(%rbp)
    # Assignment -- End line: 99
    # Assignment -- Start line: 100
    # Call -- Start line: 100
    pushq %rdi
    pushq %rsi
    # IdentifierExp -- Start line: 100
    movq -24(%rbp), %rax
    # IdentifierExp -- End line: 100
    movq %rax, %rdi
    pushq %rax
    # IdentifierExp -- Start line: 100
    movq %rsi ,  %rax
    # IdentifierExp -- End line: 100
    movq %rax, %rsi
    # IdentifierExp -- Start line: 100
    movq -16(%rbp), %rax
    # IdentifierExp -- End line: 100
    movq %rax, %rdx
    movq $0, %rax
    movq %rax, %rcx
    popq %rax
    movq %rax, %rdi
    movq (%rax), %r10
    call *48(%r10)
    popq %rsi
    popq %rdi
    # Call -- End line: 100
    movq %rax, -8(%rbp)
    # Assignment -- End line: 100
    # IdentifierExp -- Start line: 101
    movq -24(%rbp), %rax
    # IdentifierExp -- End line: 101
    # Epilogue start
    movq %rbp, %rsp
    popq %rbp
    ret
    # Epilogue end
List$SetNext$:
    # Prologue start
    pushq %rbp
    movq %rsp, %rbp
    subq $0, %rsp
    # Prologue end
    # Assignment -- Start line: 107
    # IdentifierExp -- Start line: 107
    movq %rsi ,  %rax
    # IdentifierExp -- End line: 107
    movq %rax ,     16(%rdi)
    # Assignment -- End line: 107
    movq $1, %rax
    # Epilogue start
    movq %rbp, %rsp
    popq %rbp
    ret
    # Epilogue end
List$Delete$:
    # Prologue start
    pushq %rbp
    movq %rsp, %rbp
    subq $72, %rsp
    # Prologue end
    # Assignment -- Start line: 124
    movq %rdi, %rax
    movq %rax, -8(%rbp)
    # Assignment -- End line: 124
    # Assignment -- Start line: 125
    movq $0, %rax
    movq %rax, -16(%rbp)
    # Assignment -- End line: 125
    # Assignment -- Start line: 126
    # Minus -- Start line: 126
    movq $1, %rax
    pushq %rax
    movq $0, %rax
    popq %rbx
    subq %rbx, %rax
    # Minus -- End line: 126
    movq %rax, -64(%rbp)
    # Assignment -- End line: 126
    # Assignment -- Start line: 127
    movq %rdi, %rax
    movq %rax, -32(%rbp)
    # Assignment -- End line: 127
    # Assignment -- Start line: 128
    movq %rdi, %rax
    movq %rax, -40(%rbp)
    # Assignment -- End line: 128
    # Assignment -- Start line: 129
    # IdentifierExp -- Start line: 129
    movq 24(%rdi) ,  %rax
    # IdentifierExp -- End line: 129
    movq %rax, -48(%rbp)
    # Assignment -- End line: 129
    # Assignment -- Start line: 130
    # IdentifierExp -- Start line: 130
    movq 8(%rdi) ,  %rax
    # IdentifierExp -- End line: 130
    movq %rax, -56(%rbp)
    # Assignment -- End line: 130
WHILE_CONDITION9:
    # IdentifierExp -- Start line: 131
    movq -48(%rbp), %rax
    # IdentifierExp -- End line: 131
    # Not -- Start line: 131
    pushq %r10
    movq $1, %r10
    subq %rax, %r10
    movq %r10, %rax
    popq %r10
    # Not -- End line: 131
    movq %rax, %r10
    # IdentifierExp -- Start line: 131
    movq -16(%rbp), %rax
    # IdentifierExp -- End line: 131
    # Not -- Start line: 131
    pushq %r10
    movq $1, %r10
    subq %rax, %r10
    movq %r10, %rax
    popq %r10
    # Not -- End line: 131
    andq %r10, %rax
    cmpq $1, %rax
    je WHILE_BODY9
    jne WHILE_CONTINUE9
WHILE_BODY9:
    # Call -- Start line: 132
    pushq %rdi
    pushq %rsi
    # IdentifierExp -- Start line: 132
    movq %rsi ,  %rax
    # IdentifierExp -- End line: 132
    movq %rax, %rdi
    pushq %rax
    # IdentifierExp -- Start line: 132
    movq -56(%rbp), %rax
    # IdentifierExp -- End line: 132
    movq %rax, %rsi
    popq %rax
    movq %rax, %rdi
    movq (%rax), %r10
    call *16(%r10)
    popq %rsi
    popq %rdi
    # Call -- End line: 132
    cmpq $1, %rax
    jne FALSE10
TRUE10:
    # Assignment -- Start line: 133
    movq $1, %rax
    movq %rax, -16(%rbp)
    # Assignment -- End line: 133
    # IdentifierExp -- Start line: 134
    movq -64(%rbp), %rax
    # IdentifierExp -- End line: 134
    movq %rax, %r10
    movq $0, %rax
    movq %rax, %r11
    cmpq %r11, %r10
    jl TRUE12
    jge FALSE12
TRUE12:
    movq $1, %rax
    jmp CONTINUE12
FALSE12:
    movq $0, %rax
    jmp CONTINUE12
CONTINUE12:
    cmpq $1, %rax
    jne FALSE11
TRUE11:
    # Assignment -- Start line: 136
    # Call -- Start line: 136
    pushq %rdi
    pushq %rsi
    # IdentifierExp -- Start line: 136
    movq -32(%rbp), %rax
    # IdentifierExp -- End line: 136
    movq %rax, %rdi
    pushq %rax
    popq %rax
    movq %rax, %rdi
    movq (%rax), %r10
    call *32(%r10)
    popq %rsi
    popq %rdi
    # Call -- End line: 136
    movq %rax, -8(%rbp)
    # Assignment -- End line: 136
    jmp CONTINUE11
FALSE11:
    # Print -- Start line: 139
    # Minus -- Start line: 139
    movq $555, %rax
    pushq %rax
    movq $0, %rax
    popq %rbx
    subq %rbx, %rax
    # Minus -- End line: 139
    pushq %rdi
    pushq %rsi
    movq %rax, %rdi
    call put
    popq %rsi
    popq %rdi
    # Print -- End line: 139
    # Assignment -- Start line: 140
    # Call -- Start line: 140
    pushq %rdi
    pushq %rsi
    # IdentifierExp -- Start line: 140
    movq -40(%rbp), %rax
    # IdentifierExp -- End line: 140
    movq %rax, %rdi
    pushq %rax
    # Call -- Start line: 140
    pushq %rdi
    pushq %rsi
    # IdentifierExp -- Start line: 140
    movq -32(%rbp), %rax
    # IdentifierExp -- End line: 140
    movq %rax, %rdi
    pushq %rax
    popq %rax
    movq %rax, %rdi
    movq (%rax), %r10
    call *32(%r10)
    popq %rsi
    popq %rdi
    # Call -- End line: 140
    movq %rax, %rsi
    popq %rax
    movq %rax, %rdi
    movq (%rax), %r10
    call *80(%r10)
    popq %rsi
    popq %rdi
    # Call -- End line: 140
    movq %rax, -24(%rbp)
    # Assignment -- End line: 140
    # Print -- Start line: 141
    # Minus -- Start line: 141
    movq $555, %rax
    pushq %rax
    movq $0, %rax
    popq %rbx
    subq %rbx, %rax
    # Minus -- End line: 141
    pushq %rdi
    pushq %rsi
    movq %rax, %rdi
    call put
    popq %rsi
    popq %rdi
    # Print -- End line: 141
    jmp CONTINUE11
CONTINUE11:
    jmp CONTINUE10
FALSE10:
    # Assignment -- Start line: 144
    movq $0, %rax
    movq %rax, -72(%rbp)
    # Assignment -- End line: 144
    jmp CONTINUE10
CONTINUE10:
    # IdentifierExp -- Start line: 145
    movq -16(%rbp), %rax
    # IdentifierExp -- End line: 145
    # Not -- Start line: 145
    pushq %r10
    movq $1, %r10
    subq %rax, %r10
    movq %r10, %rax
    popq %r10
    # Not -- End line: 145
    cmpq $1, %rax
    jne FALSE13
TRUE13:
    # Assignment -- Start line: 146
    # IdentifierExp -- Start line: 146
    movq -32(%rbp), %rax
    # IdentifierExp -- End line: 146
    movq %rax, -40(%rbp)
    # Assignment -- End line: 146
    # Assignment -- Start line: 147
    # Call -- Start line: 147
    pushq %rdi
    pushq %rsi
    # IdentifierExp -- Start line: 147
    movq -32(%rbp), %rax
    # IdentifierExp -- End line: 147
    movq %rax, %rdi
    pushq %rax
    popq %rax
    movq %rax, %rdi
    movq (%rax), %r10
    call *32(%r10)
    popq %rsi
    popq %rdi
    # Call -- End line: 147
    movq %rax, -32(%rbp)
    # Assignment -- End line: 147
    # Assignment -- Start line: 148
    # Call -- Start line: 148
    pushq %rdi
    pushq %rsi
    # IdentifierExp -- Start line: 148
    movq -32(%rbp), %rax
    # IdentifierExp -- End line: 148
    movq %rax, %rdi
    pushq %rax
    popq %rax
    movq %rax, %rdi
    movq (%rax), %r10
    call *24(%r10)
    popq %rsi
    popq %rdi
    # Call -- End line: 148
    movq %rax, -48(%rbp)
    # Assignment -- End line: 148
    # Assignment -- Start line: 149
    # Call -- Start line: 149
    pushq %rdi
    pushq %rsi
    # IdentifierExp -- Start line: 149
    movq -32(%rbp), %rax
    # IdentifierExp -- End line: 149
    movq %rax, %rdi
    pushq %rax
    popq %rax
    movq %rax, %rdi
    movq (%rax), %r10
    call *16(%r10)
    popq %rsi
    popq %rdi
    # Call -- End line: 149
    movq %rax, -56(%rbp)
    # Assignment -- End line: 149
    # Assignment -- Start line: 150
    movq $1, %rax
    movq %rax, -64(%rbp)
    # Assignment -- End line: 150
    jmp CONTINUE13
FALSE13:
    # Assignment -- Start line: 151
    movq $0, %rax
    movq %rax, -72(%rbp)
    # Assignment -- End line: 151
    jmp CONTINUE13
CONTINUE13:
    jmp WHILE_CONDITION9
WHILE_CONTINUE9:
    # IdentifierExp -- Start line: 153
    movq -8(%rbp), %rax
    # IdentifierExp -- End line: 153
    # Epilogue start
    movq %rbp, %rsp
    popq %rbp
    ret
    # Epilogue end
List$Search$:
    # Prologue start
    pushq %rbp
    movq %rsp, %rbp
    subq $40, %rsp
    # Prologue end
    # Assignment -- Start line: 165
    movq $0, %rax
    movq %rax, -8(%rbp)
    # Assignment -- End line: 165
    # Assignment -- Start line: 166
    movq %rdi, %rax
    movq %rax, -16(%rbp)
    # Assignment -- End line: 166
    # Assignment -- Start line: 167
    # IdentifierExp -- Start line: 167
    movq 24(%rdi) ,  %rax
    # IdentifierExp -- End line: 167
    movq %rax, -32(%rbp)
    # Assignment -- End line: 167
    # Assignment -- Start line: 168
    # IdentifierExp -- Start line: 168
    movq 8(%rdi) ,  %rax
    # IdentifierExp -- End line: 168
    movq %rax, -24(%rbp)
    # Assignment -- End line: 168
WHILE_CONDITION14:
    # IdentifierExp -- Start line: 169
    movq -32(%rbp), %rax
    # IdentifierExp -- End line: 169
    # Not -- Start line: 169
    pushq %r10
    movq $1, %r10
    subq %rax, %r10
    movq %r10, %rax
    popq %r10
    # Not -- End line: 169
    cmpq $1, %rax
    je WHILE_BODY14
    jne WHILE_CONTINUE14
WHILE_BODY14:
    # Call -- Start line: 170
    pushq %rdi
    pushq %rsi
    # IdentifierExp -- Start line: 170
    movq %rsi ,  %rax
    # IdentifierExp -- End line: 170
    movq %rax, %rdi
    pushq %rax
    # IdentifierExp -- Start line: 170
    movq -24(%rbp), %rax
    # IdentifierExp -- End line: 170
    movq %rax, %rsi
    popq %rax
    movq %rax, %rdi
    movq (%rax), %r10
    call *16(%r10)
    popq %rsi
    popq %rdi
    # Call -- End line: 170
    cmpq $1, %rax
    jne FALSE15
TRUE15:
    # Assignment -- Start line: 171
    movq $1, %rax
    movq %rax, -8(%rbp)
    # Assignment -- End line: 171
    jmp CONTINUE15
FALSE15:
    # Assignment -- Start line: 173
    movq $0, %rax
    movq %rax, -40(%rbp)
    # Assignment -- End line: 173
    jmp CONTINUE15
CONTINUE15:
    # Assignment -- Start line: 174
    # Call -- Start line: 174
    pushq %rdi
    pushq %rsi
    # IdentifierExp -- Start line: 174
    movq -16(%rbp), %rax
    # IdentifierExp -- End line: 174
    movq %rax, %rdi
    pushq %rax
    popq %rax
    movq %rax, %rdi
    movq (%rax), %r10
    call *32(%r10)
    popq %rsi
    popq %rdi
    # Call -- End line: 174
    movq %rax, -16(%rbp)
    # Assignment -- End line: 174
    # Assignment -- Start line: 175
    # Call -- Start line: 175
    pushq %rdi
    pushq %rsi
    # IdentifierExp -- Start line: 175
    movq -16(%rbp), %rax
    # IdentifierExp -- End line: 175
    movq %rax, %rdi
    pushq %rax
    popq %rax
    movq %rax, %rdi
    movq (%rax), %r10
    call *24(%r10)
    popq %rsi
    popq %rdi
    # Call -- End line: 175
    movq %rax, -32(%rbp)
    # Assignment -- End line: 175
    # Assignment -- Start line: 176
    # Call -- Start line: 176
    pushq %rdi
    pushq %rsi
    # IdentifierExp -- Start line: 176
    movq -16(%rbp), %rax
    # IdentifierExp -- End line: 176
    movq %rax, %rdi
    pushq %rax
    popq %rax
    movq %rax, %rdi
    movq (%rax), %r10
    call *16(%r10)
    popq %rsi
    popq %rdi
    # Call -- End line: 176
    movq %rax, -24(%rbp)
    # Assignment -- End line: 176
    jmp WHILE_CONDITION14
WHILE_CONTINUE14:
    # IdentifierExp -- Start line: 178
    movq -8(%rbp), %rax
    # IdentifierExp -- End line: 178
    # Epilogue start
    movq %rbp, %rsp
    popq %rbp
    ret
    # Epilogue end
List$GetEnd$:
    # Prologue start
    pushq %rbp
    movq %rsp, %rbp
    subq $0, %rsp
    # Prologue end
    # IdentifierExp -- Start line: 182
    movq 24(%rdi) ,  %rax
    # IdentifierExp -- End line: 182
    # Epilogue start
    movq %rbp, %rsp
    popq %rbp
    ret
    # Epilogue end
List$GetElem$:
    # Prologue start
    pushq %rbp
    movq %rsp, %rbp
    subq $0, %rsp
    # Prologue end
    # IdentifierExp -- Start line: 186
    movq 8(%rdi) ,  %rax
    # IdentifierExp -- End line: 186
    # Epilogue start
    movq %rbp, %rsp
    popq %rbp
    ret
    # Epilogue end
List$GetNext$:
    # Prologue start
    pushq %rbp
    movq %rsp, %rbp
    subq $0, %rsp
    # Prologue end
    # IdentifierExp -- Start line: 190
    movq 16(%rdi) ,  %rax
    # IdentifierExp -- End line: 190
    # Epilogue start
    movq %rbp, %rsp
    popq %rbp
    ret
    # Epilogue end
List$Print$:
    # Prologue start
    pushq %rbp
    movq %rsp, %rbp
    subq $24, %rsp
    # Prologue end
    # Assignment -- Start line: 200
    movq %rdi, %rax
    movq %rax, -8(%rbp)
    # Assignment -- End line: 200
    # Assignment -- Start line: 201
    # IdentifierExp -- Start line: 201
    movq 24(%rdi) ,  %rax
    # IdentifierExp -- End line: 201
    movq %rax, -16(%rbp)
    # Assignment -- End line: 201
    # Assignment -- Start line: 202
    # IdentifierExp -- Start line: 202
    movq 8(%rdi) ,  %rax
    # IdentifierExp -- End line: 202
    movq %rax, -24(%rbp)
    # Assignment -- End line: 202
WHILE_CONDITION16:
    # IdentifierExp -- Start line: 203
    movq -16(%rbp), %rax
    # IdentifierExp -- End line: 203
    # Not -- Start line: 203
    pushq %r10
    movq $1, %r10
    subq %rax, %r10
    movq %r10, %rax
    popq %r10
    # Not -- End line: 203
    cmpq $1, %rax
    je WHILE_BODY16
    jne WHILE_CONTINUE16
WHILE_BODY16:
    # Print -- Start line: 204
    # Call -- Start line: 204
    pushq %rdi
    # IdentifierExp -- Start line: 204
    movq -24(%rbp), %rax
    # IdentifierExp -- End line: 204
    movq %rax, %rdi
    pushq %rax
    popq %rax
    movq %rax, %rdi
    movq (%rax), %r10
    call *24(%r10)
    popq %rdi
    # Call -- End line: 204
    pushq %rdi
    movq %rax, %rdi
    call put
    popq %rdi
    # Print -- End line: 204
    # Assignment -- Start line: 205
    # Call -- Start line: 205
    pushq %rdi
    # IdentifierExp -- Start line: 205
    movq -8(%rbp), %rax
    # IdentifierExp -- End line: 205
    movq %rax, %rdi
    pushq %rax
    popq %rax
    movq %rax, %rdi
    movq (%rax), %r10
    call *32(%r10)
    popq %rdi
    # Call -- End line: 205
    movq %rax, -8(%rbp)
    # Assignment -- End line: 205
    # Assignment -- Start line: 206
    # Call -- Start line: 206
    pushq %rdi
    # IdentifierExp -- Start line: 206
    movq -8(%rbp), %rax
    # IdentifierExp -- End line: 206
    movq %rax, %rdi
    pushq %rax
    popq %rax
    movq %rax, %rdi
    movq (%rax), %r10
    call *24(%r10)
    popq %rdi
    # Call -- End line: 206
    movq %rax, -16(%rbp)
    # Assignment -- End line: 206
    # Assignment -- Start line: 207
    # Call -- Start line: 207
    pushq %rdi
    # IdentifierExp -- Start line: 207
    movq -8(%rbp), %rax
    # IdentifierExp -- End line: 207
    movq %rax, %rdi
    pushq %rax
    popq %rax
    movq %rax, %rdi
    movq (%rax), %r10
    call *16(%r10)
    popq %rdi
    # Call -- End line: 207
    movq %rax, -24(%rbp)
    # Assignment -- End line: 207
    jmp WHILE_CONDITION16
WHILE_CONTINUE16:
    movq $1, %rax
    # Epilogue start
    movq %rbp, %rsp
    popq %rbp
    ret
    # Epilogue end
LL$Start$:
    # Prologue start
    pushq %rbp
    movq %rsp, %rbp
    subq $48, %rsp
    # Prologue end
    # Assignment -- Start line: 228
    # New Object -- Start line: 228
    pushq %rdi
    movq $32 , %rdi
    call mjcalloc
    leaq List$$, %rdx
    movq %rdx, 0(%rax)
    popq %rdi
    # New Object -- end line: 228
    movq %rax, -16(%rbp)
    # Assignment -- End line: 228
    # Assignment -- Start line: 229
    # Call -- Start line: 229
    pushq %rdi
    # IdentifierExp -- Start line: 229
    movq -16(%rbp), %rax
    # IdentifierExp -- End line: 229
    movq %rax, %rdi
    pushq %rax
    popq %rax
    movq %rax, %rdi
    movq (%rax), %r10
    call *40(%r10)
    popq %rdi
    # Call -- End line: 229
    movq %rax, -24(%rbp)
    # Assignment -- End line: 229
    # Assignment -- Start line: 230
    # IdentifierExp -- Start line: 230
    movq -16(%rbp), %rax
    # IdentifierExp -- End line: 230
    movq %rax, -8(%rbp)
    # Assignment -- End line: 230
    # Assignment -- Start line: 231
    # Call -- Start line: 231
    pushq %rdi
    # IdentifierExp -- Start line: 231
    movq -8(%rbp), %rax
    # IdentifierExp -- End line: 231
    movq %rax, %rdi
    pushq %rax
    popq %rax
    movq %rax, %rdi
    movq (%rax), %r10
    call *40(%r10)
    popq %rdi
    # Call -- End line: 231
    movq %rax, -24(%rbp)
    # Assignment -- End line: 231
    # Assignment -- Start line: 232
    # Call -- Start line: 232
    pushq %rdi
    # IdentifierExp -- Start line: 232
    movq -8(%rbp), %rax
    # IdentifierExp -- End line: 232
    movq %rax, %rdi
    pushq %rax
    popq %rax
    movq %rax, %rdi
    movq (%rax), %r10
    call *64(%r10)
    popq %rdi
    # Call -- End line: 232
    movq %rax, -24(%rbp)
    # Assignment -- End line: 232
    # Assignment -- Start line: 235
    # New Object -- Start line: 235
    pushq %rdi
    movq $32 , %rdi
    call mjcalloc
    leaq Element$$, %rdx
    movq %rdx, 0(%rax)
    popq %rdi
    # New Object -- end line: 235
    movq %rax, -32(%rbp)
    # Assignment -- End line: 235
    # Assignment -- Start line: 236
    # Call -- Start line: 236
    pushq %rdi
    # IdentifierExp -- Start line: 236
    movq -32(%rbp), %rax
    # IdentifierExp -- End line: 236
    movq %rax, %rdi
    pushq %rax
    movq $25, %rax
    movq %rax, %rsi
    movq $37000, %rax
    movq %rax, %rdx
    movq $0, %rax
    movq %rax, %rcx
    popq %rax
    movq %rax, %rdi
    movq (%rax), %r10
    call *48(%r10)
    popq %rdi
    # Call -- End line: 236
    movq %rax, -24(%rbp)
    # Assignment -- End line: 236
    # Assignment -- Start line: 237
    # Call -- Start line: 237
    pushq %rdi
    # IdentifierExp -- Start line: 237
    movq -8(%rbp), %rax
    # IdentifierExp -- End line: 237
    movq %rax, %rdi
    pushq %rax
    # IdentifierExp -- Start line: 237
    movq -32(%rbp), %rax
    # IdentifierExp -- End line: 237
    movq %rax, %rsi
    popq %rax
    movq %rax, %rdi
    movq (%rax), %r10
    call *56(%r10)
    popq %rdi
    # Call -- End line: 237
    movq %rax, -8(%rbp)
    # Assignment -- End line: 237
    # Assignment -- Start line: 238
    # Call -- Start line: 238
    pushq %rdi
    # IdentifierExp -- Start line: 238
    movq -8(%rbp), %rax
    # IdentifierExp -- End line: 238
    movq %rax, %rdi
    pushq %rax
    popq %rax
    movq %rax, %rdi
    movq (%rax), %r10
    call *64(%r10)
    popq %rdi
    # Call -- End line: 238
    movq %rax, -24(%rbp)
    # Assignment -- End line: 238
    # Print -- Start line: 239
    movq $10000000, %rax
    pushq %rdi
    movq %rax, %rdi
    call put
    popq %rdi
    # Print -- End line: 239
    # Assignment -- Start line: 241
    # New Object -- Start line: 241
    pushq %rdi
    movq $32 , %rdi
    call mjcalloc
    leaq Element$$, %rdx
    movq %rdx, 0(%rax)
    popq %rdi
    # New Object -- end line: 241
    movq %rax, -32(%rbp)
    # Assignment -- End line: 241
    # Assignment -- Start line: 242
    # Call -- Start line: 242
    pushq %rdi
    # IdentifierExp -- Start line: 242
    movq -32(%rbp), %rax
    # IdentifierExp -- End line: 242
    movq %rax, %rdi
    pushq %rax
    movq $39, %rax
    movq %rax, %rsi
    movq $42000, %rax
    movq %rax, %rdx
    movq $1, %rax
    movq %rax, %rcx
    popq %rax
    movq %rax, %rdi
    movq (%rax), %r10
    call *48(%r10)
    popq %rdi
    # Call -- End line: 242
    movq %rax, -24(%rbp)
    # Assignment -- End line: 242
    # Assignment -- Start line: 243
    # IdentifierExp -- Start line: 243
    movq -32(%rbp), %rax
    # IdentifierExp -- End line: 243
    movq %rax, -40(%rbp)
    # Assignment -- End line: 243
    # Assignment -- Start line: 244
    # Call -- Start line: 244
    pushq %rdi
    # IdentifierExp -- Start line: 244
    movq -8(%rbp), %rax
    # IdentifierExp -- End line: 244
    movq %rax, %rdi
    pushq %rax
    # IdentifierExp -- Start line: 244
    movq -32(%rbp), %rax
    # IdentifierExp -- End line: 244
    movq %rax, %rsi
    popq %rax
    movq %rax, %rdi
    movq (%rax), %r10
    call *56(%r10)
    popq %rdi
    # Call -- End line: 244
    movq %rax, -8(%rbp)
    # Assignment -- End line: 244
    # Assignment -- Start line: 245
    # Call -- Start line: 245
    pushq %rdi
    # IdentifierExp -- Start line: 245
    movq -8(%rbp), %rax
    # IdentifierExp -- End line: 245
    movq %rax, %rdi
    pushq %rax
    popq %rax
    movq %rax, %rdi
    movq (%rax), %r10
    call *64(%r10)
    popq %rdi
    # Call -- End line: 245
    movq %rax, -24(%rbp)
    # Assignment -- End line: 245
    # Print -- Start line: 246
    movq $10000000, %rax
    pushq %rdi
    movq %rax, %rdi
    call put
    popq %rdi
    # Print -- End line: 246
    # Assignment -- Start line: 248
    # New Object -- Start line: 248
    pushq %rdi
    movq $32 , %rdi
    call mjcalloc
    leaq Element$$, %rdx
    movq %rdx, 0(%rax)
    popq %rdi
    # New Object -- end line: 248
    movq %rax, -32(%rbp)
    # Assignment -- End line: 248
    # Assignment -- Start line: 249
    # Call -- Start line: 249
    pushq %rdi
    # IdentifierExp -- Start line: 249
    movq -32(%rbp), %rax
    # IdentifierExp -- End line: 249
    movq %rax, %rdi
    pushq %rax
    movq $22, %rax
    movq %rax, %rsi
    movq $34000, %rax
    movq %rax, %rdx
    movq $0, %rax
    movq %rax, %rcx
    popq %rax
    movq %rax, %rdi
    movq (%rax), %r10
    call *48(%r10)
    popq %rdi
    # Call -- End line: 249
    movq %rax, -24(%rbp)
    # Assignment -- End line: 249
    # Assignment -- Start line: 250
    # Call -- Start line: 250
    pushq %rdi
    # IdentifierExp -- Start line: 250
    movq -8(%rbp), %rax
    # IdentifierExp -- End line: 250
    movq %rax, %rdi
    pushq %rax
    # IdentifierExp -- Start line: 250
    movq -32(%rbp), %rax
    # IdentifierExp -- End line: 250
    movq %rax, %rsi
    popq %rax
    movq %rax, %rdi
    movq (%rax), %r10
    call *56(%r10)
    popq %rdi
    # Call -- End line: 250
    movq %rax, -8(%rbp)
    # Assignment -- End line: 250
    # Assignment -- Start line: 251
    # Call -- Start line: 251
    pushq %rdi
    # IdentifierExp -- Start line: 251
    movq -8(%rbp), %rax
    # IdentifierExp -- End line: 251
    movq %rax, %rdi
    pushq %rax
    popq %rax
    movq %rax, %rdi
    movq (%rax), %r10
    call *64(%r10)
    popq %rdi
    # Call -- End line: 251
    movq %rax, -24(%rbp)
    # Assignment -- End line: 251
    # Assignment -- Start line: 252
    # New Object -- Start line: 252
    pushq %rdi
    movq $32 , %rdi
    call mjcalloc
    leaq Element$$, %rdx
    movq %rdx, 0(%rax)
    popq %rdi
    # New Object -- end line: 252
    movq %rax, -48(%rbp)
    # Assignment -- End line: 252
    # Assignment -- Start line: 253
    # Call -- Start line: 253
    pushq %rdi
    # IdentifierExp -- Start line: 253
    movq -48(%rbp), %rax
    # IdentifierExp -- End line: 253
    movq %rax, %rdi
    pushq %rax
    movq $27, %rax
    movq %rax, %rsi
    movq $34000, %rax
    movq %rax, %rdx
    movq $0, %rax
    movq %rax, %rcx
    popq %rax
    movq %rax, %rdi
    movq (%rax), %r10
    call *48(%r10)
    popq %rdi
    # Call -- End line: 253
    movq %rax, -24(%rbp)
    # Assignment -- End line: 253
    # Print -- Start line: 254
    # Call -- Start line: 254
    pushq %rdi
    # IdentifierExp -- Start line: 254
    movq -8(%rbp), %rax
    # IdentifierExp -- End line: 254
    movq %rax, %rdi
    pushq %rax
    # IdentifierExp -- Start line: 254
    movq -40(%rbp), %rax
    # IdentifierExp -- End line: 254
    movq %rax, %rsi
    popq %rax
    movq %rax, %rdi
    movq (%rax), %r10
    call *72(%r10)
    popq %rdi
    # Call -- End line: 254
    pushq %rdi
    movq %rax, %rdi
    call put
    popq %rdi
    # Print -- End line: 254
    # Print -- Start line: 255
    # Call -- Start line: 255
    pushq %rdi
    # IdentifierExp -- Start line: 255
    movq -8(%rbp), %rax
    # IdentifierExp -- End line: 255
    movq %rax, %rdi
    pushq %rax
    # IdentifierExp -- Start line: 255
    movq -48(%rbp), %rax
    # IdentifierExp -- End line: 255
    movq %rax, %rsi
    popq %rax
    movq %rax, %rdi
    movq (%rax), %r10
    call *72(%r10)
    popq %rdi
    # Call -- End line: 255
    pushq %rdi
    movq %rax, %rdi
    call put
    popq %rdi
    # Print -- End line: 255
    # Print -- Start line: 256
    movq $10000000, %rax
    pushq %rdi
    movq %rax, %rdi
    call put
    popq %rdi
    # Print -- End line: 256
    # Assignment -- Start line: 258
    # New Object -- Start line: 258
    pushq %rdi
    movq $32 , %rdi
    call mjcalloc
    leaq Element$$, %rdx
    movq %rdx, 0(%rax)
    popq %rdi
    # New Object -- end line: 258
    movq %rax, -32(%rbp)
    # Assignment -- End line: 258
    # Assignment -- Start line: 259
    # Call -- Start line: 259
    pushq %rdi
    # IdentifierExp -- Start line: 259
    movq -32(%rbp), %rax
    # IdentifierExp -- End line: 259
    movq %rax, %rdi
    pushq %rax
    movq $28, %rax
    movq %rax, %rsi
    movq $35000, %rax
    movq %rax, %rdx
    movq $0, %rax
    movq %rax, %rcx
    popq %rax
    movq %rax, %rdi
    movq (%rax), %r10
    call *48(%r10)
    popq %rdi
    # Call -- End line: 259
    movq %rax, -24(%rbp)
    # Assignment -- End line: 259
    # Assignment -- Start line: 260
    # Call -- Start line: 260
    pushq %rdi
    # IdentifierExp -- Start line: 260
    movq -8(%rbp), %rax
    # IdentifierExp -- End line: 260
    movq %rax, %rdi
    pushq %rax
    # IdentifierExp -- Start line: 260
    movq -32(%rbp), %rax
    # IdentifierExp -- End line: 260
    movq %rax, %rsi
    popq %rax
    movq %rax, %rdi
    movq (%rax), %r10
    call *56(%r10)
    popq %rdi
    # Call -- End line: 260
    movq %rax, -8(%rbp)
    # Assignment -- End line: 260
    # Assignment -- Start line: 261
    # Call -- Start line: 261
    pushq %rdi
    # IdentifierExp -- Start line: 261
    movq -8(%rbp), %rax
    # IdentifierExp -- End line: 261
    movq %rax, %rdi
    pushq %rax
    popq %rax
    movq %rax, %rdi
    movq (%rax), %r10
    call *64(%r10)
    popq %rdi
    # Call -- End line: 261
    movq %rax, -24(%rbp)
    # Assignment -- End line: 261
    # Print -- Start line: 262
    movq $2220000, %rax
    pushq %rdi
    movq %rax, %rdi
    call put
    popq %rdi
    # Print -- End line: 262
    # Assignment -- Start line: 264
    # Call -- Start line: 264
    pushq %rdi
    # IdentifierExp -- Start line: 264
    movq -8(%rbp), %rax
    # IdentifierExp -- End line: 264
    movq %rax, %rdi
    pushq %rax
    # IdentifierExp -- Start line: 264
    movq -40(%rbp), %rax
    # IdentifierExp -- End line: 264
    movq %rax, %rsi
    popq %rax
    movq %rax, %rdi
    movq (%rax), %r10
    call *8(%r10)
    popq %rdi
    # Call -- End line: 264
    movq %rax, -8(%rbp)
    # Assignment -- End line: 264
    # Assignment -- Start line: 265
    # Call -- Start line: 265
    pushq %rdi
    # IdentifierExp -- Start line: 265
    movq -8(%rbp), %rax
    # IdentifierExp -- End line: 265
    movq %rax, %rdi
    pushq %rax
    popq %rax
    movq %rax, %rdi
    movq (%rax), %r10
    call *64(%r10)
    popq %rdi
    # Call -- End line: 265
    movq %rax, -24(%rbp)
    # Assignment -- End line: 265
    # Print -- Start line: 266
    movq $33300000, %rax
    pushq %rdi
    movq %rax, %rdi
    call put
    popq %rdi
    # Print -- End line: 266
    # Assignment -- Start line: 269
    # Call -- Start line: 269
    pushq %rdi
    # IdentifierExp -- Start line: 269
    movq -8(%rbp), %rax
    # IdentifierExp -- End line: 269
    movq %rax, %rdi
    pushq %rax
    # IdentifierExp -- Start line: 269
    movq -32(%rbp), %rax
    # IdentifierExp -- End line: 269
    movq %rax, %rsi
    popq %rax
    movq %rax, %rdi
    movq (%rax), %r10
    call *8(%r10)
    popq %rdi
    # Call -- End line: 269
    movq %rax, -8(%rbp)
    # Assignment -- End line: 269
    # Assignment -- Start line: 270
    # Call -- Start line: 270
    pushq %rdi
    # IdentifierExp -- Start line: 270
    movq -8(%rbp), %rax
    # IdentifierExp -- End line: 270
    movq %rax, %rdi
    pushq %rax
    popq %rax
    movq %rax, %rdi
    movq (%rax), %r10
    call *64(%r10)
    popq %rdi
    # Call -- End line: 270
    movq %rax, -24(%rbp)
    # Assignment -- End line: 270
    # Print -- Start line: 271
    movq $44440000, %rax
    pushq %rdi
    movq %rax, %rdi
    call put
    popq %rdi
    # Print -- End line: 271
    movq $0, %rax
    # Epilogue start
    movq %rbp, %rsp
    popq %rbp
    ret
    # Epilogue end

