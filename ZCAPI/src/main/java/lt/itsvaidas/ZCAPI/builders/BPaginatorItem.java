package lt.itsvaidas.ZCAPI.builders;

public record BPaginatorItem(BItem item, Runnable run) {
    public static BPaginatorItem b(BItem item, Runnable run) {
        return new BPaginatorItem(item, run);
    }
}
