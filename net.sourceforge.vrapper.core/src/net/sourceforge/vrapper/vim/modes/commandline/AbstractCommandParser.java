package net.sourceforge.vrapper.vim.modes.commandline;

import static net.sourceforge.vrapper.keymap.vim.ConstructorWrappers.key;
import net.sourceforge.vrapper.keymap.KeyStroke;
import net.sourceforge.vrapper.keymap.SpecialKey;
import net.sourceforge.vrapper.vim.EditorAdaptor;
import net.sourceforge.vrapper.vim.modes.NormalMode;

/**
 * Base class for modes which parse strings given by the user.<br>
 * Shows the input in the {@link Platform}'s command line.
 *
 * @author Matthias Radig
 */
public abstract class AbstractCommandParser {

    private static final KeyStroke KEY_RETURN = key(SpecialKey.RETURN);
    private static final KeyStroke KEY_ESCAPE = key(SpecialKey.ESC);
    private static final KeyStroke KEY_BACKSP = key(SpecialKey.BACKSPACE);
    protected final StringBuffer buffer;
    protected final EditorAdaptor editor;

    public AbstractCommandParser(EditorAdaptor vim) {
        this.editor = vim;
        buffer = new StringBuffer();
    }

    /**
     * Appends typed characters to the internal buffer. Deletes a char from the
     * buffer on press of the backspace key. Parses and executes the buffer on
     * press of the return key. Clears the buffer on press of the escape key.
     */
    public void type(KeyStroke e) {
        if (e.equals(KEY_RETURN)) {
            parseAndExecute();
        } else if (e.equals(KEY_BACKSP)) {
            buffer.setLength(buffer.length()-1);
        } else {
            buffer.append(e.getCharacter());
        }
        if (buffer.length() == 0 || e.equals(KEY_RETURN) || e.equals(KEY_ESCAPE)) {
            editor.changeMode(NormalMode.NAME);
        }
    }

    public String getBuffer() {
        return buffer.toString();
    }

    /**
     * Parses and executes the given command if possible.
     * 
     * @param first
     *            character used to activate the mode.
     * @param command
     *            the command to execute.
     */
    public abstract void parseAndExecute(String first, String command);

    private void parseAndExecute() {
        String first = buffer.substring(0,1);
        String command = buffer.substring(1, buffer.length());
        parseAndExecute(first, command);
    }

}