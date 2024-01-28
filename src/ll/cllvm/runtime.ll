@LONG = constant i32 0

%TypeTag = type { i32 }

%Primitive = type { %TypeTag, [8 x i8] }

new TaggedExpr(some expr, @DOUBLE)

@result_area = global %Primitive zeroinitializer