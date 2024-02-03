; ModuleID = 'user'
source_filename = "user.ll"
%TypeTag = type { i32 }
%Primitive = type { %TypeTag, [8 x i8] }
define %Primitive* @eval() {
entry:
%ptr_0 = alloca %Primitive, align 8
%ptr_1 = getelementptr %Primitive, %Primitive* %ptr_0, i32 0, i32 0
store i32 0, i32* %ptr_1
%ptr_2 = getelementptr %Primitive, %Primitive* %ptr_0, i32 0, i32 1
%ptr_3 = bitcast [8 x i8]* %ptr_2 to i64*
store i64 19, i64* %ptr_3, align 8
ret %Primitive* %ptr_0
}