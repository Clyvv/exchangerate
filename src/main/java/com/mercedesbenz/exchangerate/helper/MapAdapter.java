package com.mercedesbenz.exchangerate.helper;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.HashMap;
import java.util.Map;

public class MapAdapter extends XmlAdapter<PairList, Map<String, Double>>
{
    @Override
    public Map<String, Double> unmarshal(PairList list) throws Exception
    {
        Map<String, Double> retVal = new HashMap<String, Double>();
        for (Pair keyValue : list.getValues())
        {
            retVal.put(keyValue.getKey(), keyValue.getValue());
        }
        return retVal;
    }

    @Override
    public PairList marshal(Map<String, Double> map) throws Exception
    {
        PairList retVal = new PairList();
        for (String key : map.keySet())
        {
            retVal.getValues().add(new Pair(key, map.get(key)));
        }
        return retVal;
    }
}
