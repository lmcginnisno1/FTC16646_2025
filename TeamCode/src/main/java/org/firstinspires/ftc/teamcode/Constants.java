package org.firstinspires.ftc.teamcode;

public class Constants {
    public static final class BucketConstants{
        public static final double kBucketHome = 0.785;
        public static final double kBucketDeploy = 0.95;
        public static final double kBucketReceive = 0.7;//0.725
    }
    public static final class SubIntakeConstants{
        public static final double kBucketHome = 0.25;//0.3
        public static final double kBucketTransfer = 0.22;//0.225
        public static final double kBucketReadyToIntake = 0.65;//0.65
        public static final double kBucketIntake = 0.765;//.765
        public static final double kIntakeOn = 1;
        public static final double kIntakeOff = 0;
        public static final double kIntakeReverse = -0.25;
        public static final double kIntakeTransfer = 0.1;
    }
    public static final class SubmersibleSlide{
        public static final int kSlideIntake = 750;
        public static final int kSlideTransfer = 100;//300
        public static final int kSlideMaxExtend = 1000;
        public static final int kSlideTolerance = 10;
    }
    public static final class ClimbConstants{
        public static final int kClimb = 6300;
    }

    public static final class BucketLift{
        public static final int kLiftHome = 5;
        public static final int kLiftReadyToIntakeWall = 60;
        public static final int kLiftIntakeWall = 120;
        public static final int kLiftReadyToDeployHighBasket = 1080;
        public static final int kLiftReadyToDeployHighChamber = 550;
        public static final int kLiftDeployHighChamber = 400;
        public static final int kLiftTolerance = 5;
    }
}
