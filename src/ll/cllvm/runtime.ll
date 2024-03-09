; ModuleID = 'runtime.c'
source_filename = "runtime.c"

%struct.Primitive = type { i32, ptr }

@.str = private unnamed_addr constant [5 x i8] c"%ld\0A\00", align 1
@.str.1 = private unnamed_addr constant [4 x i8] c"%f\0A\00", align 1
@.str.2 = private unnamed_addr constant [4 x i8] c"%s\0A\00", align 1

; Function Attrs: noinline nounwind optnone uwtable
define dso_local i32 @main() #0 {
  %1 = alloca i32, align 4
  %2 = alloca ptr, align 8
  store i32 0, ptr %1, align 4
  %3 = call ptr @eval()
  store ptr %3, ptr %2, align 8
  %4 = load ptr, ptr %2, align 8
  %5 = getelementptr inbounds %struct.Primitive, ptr %4, i32 0, i32 0
  %6 = load i32, ptr %5, align 8
  switch i32 %6, label %24 [
    i32 0, label %7
    i32 1, label %13
    i32 2, label %19
  ]

7:                                                ; preds = %0
  %8 = load ptr, ptr %2, align 8
  %9 = getelementptr inbounds %struct.Primitive, ptr %8, i32 0, i32 1
  %10 = load ptr, ptr %9, align 8
  %11 = load i64, ptr %10, align 8
  %12 = call i32 (ptr, ...) @printf(ptr noundef @.str, i64 noundef %11)
  br label %24

13:                                               ; preds = %0
  %14 = load ptr, ptr %2, align 8
  %15 = getelementptr inbounds %struct.Primitive, ptr %14, i32 0, i32 1
  %16 = load ptr, ptr %15, align 8
  %17 = load double, ptr %16, align 8
  %18 = call i32 (ptr, ...) @printf(ptr noundef @.str.1, double noundef %17)
  br label %24

19:                                               ; preds = %0
  %20 = load ptr, ptr %2, align 8
  %21 = getelementptr inbounds %struct.Primitive, ptr %20, i32 0, i32 1
  %22 = load ptr, ptr %21, align 8
  %23 = call i32 (ptr, ...) @printf(ptr noundef @.str.2, ptr noundef %22)
  br label %24

24:                                               ; preds = %0, %19, %13, %7
  %25 = load i32, ptr %1, align 4
  ret i32 %25
}

declare ptr @eval() #1

declare i32 @printf(ptr noundef, ...) #1

attributes #0 = { noinline nounwind optnone uwtable "frame-pointer"="all" "min-legal-vector-width"="0" "no-trapping-math"="true" "stack-protector-buffer-size"="8" "target-cpu"="x86-64" "target-features"="+cmov,+cx8,+fxsr,+mmx,+sse,+sse2,+x87" "tune-cpu"="generic" }
attributes #1 = { "frame-pointer"="all" "no-trapping-math"="true" "stack-protector-buffer-size"="8" "target-cpu"="x86-64" "target-features"="+cmov,+cx8,+fxsr,+mmx,+sse,+sse2,+x87" "tune-cpu"="generic" }

!llvm.module.flags = !{!0, !1, !2, !3, !4}
!llvm.ident = !{!5}

!0 = !{i32 1, !"wchar_size", i32 4}
!1 = !{i32 8, !"PIC Level", i32 2}
!2 = !{i32 7, !"PIE Level", i32 2}
!3 = !{i32 7, !"uwtable", i32 2}
!4 = !{i32 7, !"frame-pointer", i32 2}
!5 = !{!"Homebrew clang version 17.0.6"}
