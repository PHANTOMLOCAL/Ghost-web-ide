package Ninja.coder.Ghostemane.code.terminal.key;

import androidx.annotation.NonNull;

import java.util.HashMap;

/**
 * The {@link Class} that implements special buttons for {@link VirtualKeysView}.
 */
public class SpecialButton {

    public static final SpecialButton CTRL = new SpecialButton("CTRL");
    public static final SpecialButton ALT = new SpecialButton("ALT");
    public static final SpecialButton SHIFT = new SpecialButton("SHIFT");
    public static final SpecialButton FN = new SpecialButton("FN");
    private static final HashMap<String, SpecialButton> map = new HashMap<>();
    /**
     * The special button key.
     */
    private final String key;

    /**
     * Initialize a {@link SpecialButton}.
     *
     * @param key The unique key name for the special button. The key is registered in {@link #map}
     *            with which the {@link SpecialButton} can be retrieved via a call to {@link
     *            #valueOf(String)}.
     */
    public SpecialButton(@NonNull final String key) {
        this.key = key;
        map.put(key, this);
    }

    /**
     * Get the {@link SpecialButton} for {@code key}.
     *
     * @param key The unique key name for the special button.
     */
    public static SpecialButton valueOf(String key) {
        return map.get(key);
    }

    /**
     * Get {@link #key} for this {@link SpecialButton}.
     */
    public String getKey() {
        return key;
    }

    @NonNull
    @Override
    public String toString() {
        return key;
    }
}
