package xyz.meowricles.datamatrix.items.media;

public class CompactDiscItem extends StorageMediumBase {
    private static final int CAPACITY = 1000000;

    public CompactDiscItem(Properties props) {
        super(props, CAPACITY);
    }

}
