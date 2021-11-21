```java
public final class Category {
   private boolean hot;
   private int count;
   @NotNull
   private String name;

   public final boolean getHot() {
      return this.hot;
   }

   public final void setHot(boolean var1) {
      this.hot = var1;
   }

   public final int getCount() {
      return this.count;
   }

   public final void setCount(int var1) {
      this.count = var1;
   }

   @NotNull
   public final String getName() {
      return this.name;
   }

   public final void setName(@NotNull String var1) {
      Intrinsics.checkParameterIsNotNull(var1, "<set-?>");
      this.name = var1;
   }

   public Category(boolean hot, int count, @NotNull String name) {
      Intrinsics.checkParameterIsNotNull(name, "name");
      super();
      this.hot = hot;
      this.count = count;
      this.name = name;
   }
}

```