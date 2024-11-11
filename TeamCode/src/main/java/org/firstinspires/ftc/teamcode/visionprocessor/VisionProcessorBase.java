package org.firstinspires.ftc.teamcode.visionprocessor;

import org.firstinspires.ftc.vision.VisionProcessor;

public abstract class VisionProcessorBase implements VisionProcessor {
    private boolean m_redAlliance = true;

    public int getSelected(){return 0;};

    public void setRedAlliance(){
        m_redAlliance=true;
    }

    public void setBlueAlliance(){
        m_redAlliance=false;
    }

}