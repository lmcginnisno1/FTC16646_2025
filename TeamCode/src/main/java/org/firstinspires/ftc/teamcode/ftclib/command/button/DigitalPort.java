package org.firstinspires.ftc.teamcode.ftclib.command.button;

import com.qualcomm.robotcore.hardware.DigitalChannel;

public class DigitalPort extends Trigger{
     private DigitalChannel m_digitalPort;
     public DigitalPort(DigitalChannel p_port){
          m_digitalPort = p_port;
     }

     @Override
     public boolean get(){
          return m_digitalPort.getState();
     }
}
