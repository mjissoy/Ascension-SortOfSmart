package net.thejadeproject.ascension.util.math;

import net.minecraft.world.phys.Vec2;

public class CircleBoundChecker {
    public static boolean isPointInCircle(Vec2 center,Vec2 point, float radiusSquared){

        return center.distanceToSqr(point) <= radiusSquared;

    }
    //normal in this case is (1,0)

    public static double angleBetweenPointCenterAndNormal(Vec2 point,Vec2 center){
        Vec2 movedToOrigin = new Vec2(point.x-center.x,point.y-center.y);
        Vec2 normal = new Vec2(1,0);
        double dot = movedToOrigin.dot(normal);
        double mag = movedToOrigin.length()*normal.length();
        double limitedAngle = Math.acos(dot/mag);
        limitedAngle = Math.toDegrees(limitedAngle);
        if(point.y < center.y) limitedAngle = 360-limitedAngle;
        return limitedAngle;
    }
}
