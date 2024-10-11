package org.firstinspires.ftc.teamcode.ftclib.command.button;

import org.firstinspires.ftc.teamcode.ftclib.gamepad.GamepadEx;
import org.firstinspires.ftc.teamcode.ftclib.gamepad.GamepadKeys;

/**
 * A {@link Button} that gets its state from a {@link GamepadEx}.
 *
 * @author Jackson
 */
public class GamepadButton extends Button {

    private final GamepadEx m_gamepad;
    private final GamepadKeys.Button[] m_buttons;
    // private final GamepadKeys.Button m_button;

    /**
     * Creates a gamepad button for triggering commands.
     *
     * @param gamepad   the gamepad with the buttons
     * @param buttons   the specified buttons
     */
    public GamepadButton(GamepadEx gamepad, GamepadKeys.Button... buttons) {
    // public GamepadButton(GamepadEx gamepad, GamepadKeys.Button button) {
        m_gamepad = gamepad;
        m_buttons = buttons;
        // m_button = button;
    }

    /**
     * Gets the value of the joystick button.
     *
     * @return The value of the joystick button
     */
    @Override
    public boolean get() {
        boolean res = true;
        for (GamepadKeys.Button button : m_buttons)
            res = res && m_gamepad.getButton(button);
        // res = m_gamepad.getButton(m_button);
        return res;
    }

}
