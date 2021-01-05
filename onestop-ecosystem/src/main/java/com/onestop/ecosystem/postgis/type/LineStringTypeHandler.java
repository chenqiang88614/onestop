package com.onestop.ecosystem.postgis.type;

import org.apache.ibatis.type.MappedTypes;
import org.postgis.LineString;

/**
 * Created by mei on 04/09/2017.
 */
@MappedTypes(LineString.class)
public class LineStringTypeHandler extends AbstractGeometryTypeHandler<LineString>{
}
