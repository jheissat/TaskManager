package fr.julienheissat.modelController;

import android.location.Address;

/**
 * Created by juju on 15/09/2014.
 */
public class Task {

    private String name;
    private boolean complete;
    private long id;
    private String address;
    private double latitude;
    private double longitude;

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public Task(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isComplete() {
        return complete;

    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public void toggleComplete() {
        complete  = !complete;

    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public void setAddress (Address a)
    {
        if (null==a)
        {
            address = null;
            latitude=0;
            longitude=0;
        }
        else
        {
           int maxAddressLine = a.getMaxAddressLineIndex();
           StringBuffer sb = new StringBuffer("");

            for (int i=0;i<maxAddressLine;i++)
            {
            sb.append(a.getAddressLine(i)+" ");
            }

            address=sb.toString();
            latitude=a.getLatitude();
            longitude=a.getLongitude();

        }


    }

    public boolean hasAddress() {
        return (null != address);

    }

    public double getLatitude()
    {
        return latitude;
    }

    public void setLatitude(double latitude)
    {
        this.latitude = latitude;
    }

    public double getLongitude()
    {
        return longitude;
    }

    public void setLongitude(double longitude)
    {
        this.longitude = longitude;
    }

    public void setTask(long id,String name,boolean complete,String address,double latitude, double longitude)
    {
        this.id=id;
        this.name=name;
        this.complete=complete;
        this.address=address;
        this.latitude=latitude;
        this.longitude=longitude;

    }


}
