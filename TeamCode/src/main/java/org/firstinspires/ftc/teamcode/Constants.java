package org.firstinspires.ftc.teamcode;

public class Constants {
    public static final class BucketConstants{
        public static final double kBucketHome = 0.785;
        public static final double kBucketDeploy = 0.95;
        public static final double kBucketReceive = 0.7;//0.725
    }
    public static final class SubIntakeConstants{
        public static final double kBucketHome = 0.35;//0.52
        public static final double kBucketTransfer = 0.225;//0.375
        public static final double kBucketReadyToIntake = 0.65;//0.75
        public static final double kBucketIntake = 0.7633;//.89
        public static final double kIntakeOn = 1;
        public static final double kIntakeOff = 0;
        public static final double kIntakeReverse = -0.33;
    }
    public static final class SubmersibleSlide{
        public static final int kSlideIntake = 1250;
        public static final int kSlideTransfer = 400;//450
        public static final int kSlideMaxExtend = 1450;
        public static final int kSlideTolerance = 10;
    }
    public static final class ClimbConstants{
        public static final int kClimb = 9000;
    }

    public static final class BucketLift{
        public static final int kLiftHome = 3;
        public static final int kLiftReadyToIntakeWall = 250;
        public static final int kLiftIntakeWall = 500;
        public static final int kLiftReadyToDeployHighBasket = 2900;
        public static final int kLiftReadyToDeployHighChamber = 1600;
        public static final int kLiftDeployHighChamber = 850;
        public static final int kLiftTolerance = 5;
    }
}
