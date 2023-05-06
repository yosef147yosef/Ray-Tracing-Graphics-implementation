package renderer;

import primitives.*;

import java.util.ArrayList;
import java.util.List;
import java.util.MissingResourceException;

/*
*class for implement camera
*  */
public class Camera {
    /*the location of the camera*/
    Point place;
    /* three diraction vectors for the camera*/
    Vector vto ;
    Vector vup;
    Vector vright;
    /* the highet and the width and the distance between the camera and the view plane*/
    double highet ;
    double width;
    double distance;
    private ImageWriter imageWriter;
    private RayTracerBase rayTracerBase;


    public Camera setImageWriter(ImageWriter imageWriter) {
        this.imageWriter = imageWriter;
        return this;
    }


    public Camera setRayTracer(RayTracerBase rayTracerBase) {
        this.rayTracerBase = rayTracerBase;
        return this;
    }
    private Color castRay (int xIndex,int yIndex)
    {
        try
        {
            return rayTracerBase.traceRay(constructRay(imageWriter.getNx(), imageWriter.getNy(), xIndex,yIndex));
        }
        catch (MissingResourceException missingResourceException)
        {
            throw (new UnsupportedOperationException("one or more of the field is not inialized"));
        }
    }
    /**/
    public void  renderImage()
    {
        try
        {
            int nX = imageWriter.getNx();
            int ny = imageWriter.getNy();
            for (int i=0;i<ny;++i)
            {
                for (int q=0;q<nX;q++)
                {
                    imageWriter.writePixel(q,i,castRay(q,i));
                }
            }
        }
        catch (MissingResourceException e)//unvaild varibles
        {
            throw (new UnsupportedOperationException("one or more of the field is not inialized"));
        }


    }
    /*gets color and interval and paint a grid upon the image*/
    public void printGrid(int interval,Color color)
    {
        int nX = imageWriter.getNx();
        int ny = imageWriter.getNy();
        for (int i=0;i<nX;i++)
        {
            for (int q=0;q<ny;q++)
            {
                if (i%interval==0||q%interval==0) imageWriter.writePixel(i,q,color);
            }

        }
    }
    /*create image from the information*/
    public void writeToImage()
    {
        try
        {
            imageWriter.writeToImage();
        }
        catch (MissingResourceException missingResourceException)
        {
            throw (new UnsupportedOperationException("one or more of the field is not inialized"));

        }

    }
    public Camera(Point place, Vector vto, Vector vup) {

        this.place = place;
        if (vto.dotProduct(vup) != 0) throw (new IllegalArgumentException("Vector is not orthogonal"));
        this.vto = vto.normalize();
        this.vup = vup.normalize();
        this.vright = vto.crossProduct(vup).normalize();//orthogonal to vto and vup
    }
    public Camera setVPSize(double width, double height)
    {
        this.width = width;
        this.highet = height;
        return this;
    }
    public Camera setVPDistance(double distance)
    {
        this.distance =distance;
        return this;
    }
    public Ray constructRay(int nX, int nY, int j, int i) {

        Point pij = getPlace().add(vto.scale(distance));
        double yi = -(i - ((double)nY - 1) / 2) * highet / nY;
        if (yi !=0 ) pij = pij.add(vup.scale(yi));
        double xj = (j - ((double)nX - 1) / 2) * width / nX;
        if (xj !=0 ) pij = pij.add(vright.scale(xj));
        return new Ray(place,pij.subtract(place));
    }
    public Point getPlace() {
        return place;
    }

    public Vector getVto() {
        return vto;
    }

    public Vector getVup() {
        return vup;
    }

    public Vector getVright() {
        return vright;
    }

    public double getHighet() {
        return highet;
    }

    public double getWidth() {
        return width;
    }

    public double getDistance() {
        return distance;
    }
    /*spinnig function cant get 90 angle or any multipltion or it.*/
    public Camera spinX(double angle)//the functino use vector spin to change the diraction
    {

        vto = vto.spinX(angle);

        vright = vto.crossProduct(vup);
        return this;
    }
    public Camera spinY(double angle)//the functino use vector spin to change the diraction
    {
        vto = vto.spinY(angle);
        vup = vright.crossProduct(vto);
        return this;
    }
    public Camera spinZ(double angle)//the functino use vector spin to change the diraction
    {
        vto = vto.spinZ(angle);
        vup = vright.crossProduct(vto);
        return this;
    }

}