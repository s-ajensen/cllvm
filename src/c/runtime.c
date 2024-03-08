#include <stdint.h>
#include <stdio.h>

typedef int32_t TypeTag;

typedef struct {
  TypeTag typeTag;
  void* data;
} Primitive;

Primitive* eval(void);

#define LONG_TAG 0
#define DOUBLE_TAG 1

int main() {
  Primitive* output = eval();
  switch (output->typeTag) {
    case LONG_TAG:
      printf("%ld\n",*(long*)output->data);
      break;
    case DOUBLE_TAG:
      printf("%f\n",*(double*)output->data);
      break;
  }
}