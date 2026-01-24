package net.thejadeproject.ascension.util.math;

import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class BoundCheckHelpers {
    /*
        assumes they are overlapping

        not exact
     */
    public static Vec3 getSphereBoundingBoxCollisionCorner(Vec3 center, double radius, AABB boundingBox){
        double x,y,z;
        x = y = z = 0;

        //check x
        if(center.x < boundingBox.minX) x = boundingBox.minX ;
        else x = boundingBox.maxX;

        //check y
        if(center.y < boundingBox.minY) y = boundingBox.minY;
        else y = boundingBox.maxY;

        //check z
        if(center.z < boundingBox.minZ) z = boundingBox.minZ;
        else z = boundingBox.maxZ;

        return new Vec3(x,y,z);
    }
    public static boolean doesSphereOverlapBoundingBox(Vec3 center, double radius, AABB boundingBox){
        double minDistance = 0;

        //check x
        if(center.x < boundingBox.minX) minDistance += Math.pow(center.x-boundingBox.minX,2);
        else if(center.x > boundingBox.maxX) minDistance += Math.pow(center.x-boundingBox.maxX,2);

        //check y
        if(center.y < boundingBox.minY) minDistance += Math.pow(center.y-boundingBox.minY,2);
        else if(center.y > boundingBox.maxY) minDistance += Math.pow(center.y-boundingBox.maxY,2);

        //check z
        if(center.z < boundingBox.minZ) minDistance += Math.pow(center.z-boundingBox.minZ,2);
        else if(center.z > boundingBox.maxZ) minDistance += Math.pow(center.z-boundingBox.maxZ,2);

        return minDistance <= radius*radius;
    }
}
