package org.firstinspires.ftc.teamcode;

public class Constants {
    public static final class BucketConstants{
        public static final double kBucketHome = 0.8;
        public static final double kBucketDeploy = 1;
        public static final double kBucketReceive = 0.65;
    }
    public static final class SubIntakeConstants{
        public static final double kBucketHome = 0.525;
        public static final double kBucketTransfer = 0.375;
        public static final double kBucketReadyToIntake = 0.85;
        public static final double kBucketIntake = 0.95;//.965
        public static final double kIntakeOn = 1;
        public static final double kIntakeOff = 0;
        public static final double kIntakeReverse = -0.25;
    }
    public static final class BucketLift{
        public static final int kLiftHome = 3;
        public static final int kLiftReadyToIntakeWall = 250;
        public static final int kLiftIntakeWall = 500;
        public static final int kLiftReadyToDeployHighBasket = 3000;
        public static final int kLiftReadyToDeployHighChamber = 1500;
        public static final int kLiftDeployHighChamber = 850;
        public static final int kLiftTolerance = 5;
    }
    public static final class SubmersibleSlide{
        public static final int kSlideHome = 5;
        public static final int kSlideIntake = 1250;
        public static final int kSlideTransfer = 550;//450
        public static final int kSlideMaxExtend = 1450;
        public static final int kSlideTolerance = 10;
    }
    public static final class ClimbConstants{
        public static final int kHome = 0;
        public static final int kClimb = 6000;
    }
}
