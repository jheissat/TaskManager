package fr.julienheissat.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import fr.julienheissat.ui.fragment.MapShowFragment;
import fr.julienheissat.ui.fragment.TaskListFragment;

/**
 * Created by juju on 26/09/2014.
 */
public class TabPagerAdapter extends FragmentPagerAdapter
{
    public TabPagerAdapter(FragmentManager fm)
    {
        super(fm);
    }
    private static final String[] CONTENT = new String[] {"  List  ", "  Map  "};



    @Override
    public Fragment getItem(int position)
    {
        if (position==0)
        {
            return TaskListFragment.newInstance();

        }
        else if (position==1)

        {
            return MapShowFragment.newInstance();
        }
        else return TaskListFragment.newInstance();

    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        return CONTENT[position % CONTENT.length].toUpperCase();
    }

    @Override
    public int getCount()
    {
        return CONTENT.length;
    }

}

