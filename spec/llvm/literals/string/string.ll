; ModuleID = 'user'
source_filename = "user.ll"
%TypeTag = type { i32 }
%Primitive = type { %TypeTag, ptr }
define %Primitive* @eval() {
entry:
%str_8 = alloca [6 x i8], align 1
store [6 x i8] c"Hello\00", [6 x i8]* %str_8, align 1
%ptr_8 = alloca %Primitive, align 8
%ptr_9 = getelementptr %Primitive, %Primitive* %ptr_8, i32 0, i32 0
store i32 2, i32* %ptr_9
%ptr_10 = getelementptr %Primitive, %Primitive* %ptr_8, i32 0, i32 1
%ptr_11 = getelementptr [6 x i8], [6 x i8]* %str_8, i64 0, i64 0
store i8* %ptr_11, ptr %ptr_10, align 8
ret %Primitive* %ptr_8
}