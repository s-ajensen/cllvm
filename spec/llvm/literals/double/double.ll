; ModuleID = 'user'
source_filename = "user.ll"
%TypeTag = type { i32 }
%Primitive = type { %TypeTag, ptr }
define %Primitive* @eval() {
entry:
%ptr_4 = alloca %Primitive, align 8
%ptr_5 = getelementptr %Primitive, %Primitive* %ptr_4, i32 0, i32 0
store i32 1, i32* %ptr_5
%ptr_6 = getelementptr %Primitive, %Primitive* %ptr_4, i32 0, i32 1
%ptr_7 = alloca double, align 8
store double 3.14, double* %ptr_7, align 8
store double* %ptr_7, ptr %ptr_6, align 8
ret %Primitive* %ptr_4
}