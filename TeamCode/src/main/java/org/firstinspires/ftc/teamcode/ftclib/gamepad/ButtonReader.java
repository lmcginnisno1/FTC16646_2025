package org.firstinspires.ftc.teamcode.ftclib.gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.function.BooleanSupplier;

/**
 * Class that reads the value of button states.
 * In order to get any values that depend on the previous state, you must call "readValue();" in a loop.
 */
public class ButtonReader implements KeyReader {

    /** Last state of the button **/
    private boolean lastState;
    /** Current state of the button **/
    private boolean currState;
    private Telemetry telemetry;
    /*** Description of Button ***/
    private String buttonName;

    private GamepadEx m_gamepad;
    private GamepadKeys.Button m_button;

    private BooleanSupplier buttonState;
    /** Initializes controller variables
     * @param gamepad The controller joystick
     * @param button The controller button
    **/
    public ButtonReader(GamepadEx gamepad, GamepadKeys.Button button) {

        m_gamepad = gamepad;
        m_button = button;
        // buttonState = () -> gamepad.getButton(button);
        currState = gamepad.getButton(button); // buttonState.getAsBoolean();
        lastState = currState;
    }

    // public ButtonReader(BooleanSupplier buttonValue) {
    //     buttonState = buttonValue;
    //     currState = buttonState.getAsBoolean();
    //     lastState = currState;
    // }

    /** Reads button value **/
    public void readValue() {
        lastState = currState;
        currState = m_gamepad.getButton(m_button); //buttonState.getAsBoolean();
    }

    /** Checks if the button is down **/
    public boolean isDown() {
        return m_gamepad.getButton(m_button); //buttonState.getAsBoolean();
    }
    /** Checks if the button was just pressed **/
    public boolean wasJustPressed() {
        return (!lastState && currState);
    }
    /** Checks if the button was just released **/
    public boolean wasJustReleased() {
        return (lastState && !currState);
    }
    /** Checks if the button state has changed **/
    public boolean stateJustChanged() {
        return (lastState != currState);
    }

}
