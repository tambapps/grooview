package java.lang.invoke;

import java.util.concurrent.atomic.AtomicInteger;

public class SwitchPoint {
  private static final MethodHandle K_true;
  private static final MethodHandle K_false;
  private final MutableCallSite mcs;
  private final MethodHandle mcsInvoker;

  public SwitchPoint() {
    this.mcs = new MutableCallSite(K_true);
    this.mcsInvoker = this.mcs.dynamicInvoker();
  }

  public boolean hasBeenInvalidated() {
    return this.mcs.getTarget() != K_true;
  }

  public MethodHandle guardWithTest(MethodHandle var1, MethodHandle var2) {
    return this.mcs.getTarget() == K_false ? var2 : MethodHandles.guardWithTest(this.mcsInvoker, var1, var2);
  }

  public static void invalidateAll(SwitchPoint[] var0) {
    if (var0.length != 0) {
      MutableCallSite[] var1 = new MutableCallSite[var0.length];

      for(int var2 = 0; var2 < var0.length; ++var2) {
        SwitchPoint var3 = var0[var2];
        if (var3 == null) {
          break;
        }

        var1[var2] = var3.mcs;
        var3.mcs.setTarget(K_false);
      }

      syncAll(var1);
    }
  }

  private static final AtomicInteger STORE_BARRIER = new AtomicInteger();

  public static void syncAll(MutableCallSite[] sites) {
         if (sites.length == 0)  return;
         STORE_BARRIER.lazySet(0);
         for (int i = 0; i < sites.length; i++) {
             sites[i].getClass();  // trigger NPE on first null
         }
     }
  static {
    K_true = MethodHandles.constant(Boolean.TYPE, true);
    K_false = MethodHandles.constant(Boolean.TYPE, false);
  }
}