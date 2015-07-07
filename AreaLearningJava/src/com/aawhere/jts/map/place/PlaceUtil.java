package com.aawhere.jts.map.place;

import com.aawhere.lang.string.StringUtilsExtended;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multimaps;
import com.vividsolutions.jts.geom.Point;

import javax.annotation.Nullable;

/**
 * Created by aroller on 7/2/15.
 */
public class PlaceUtil {


    public static Point[] points(Iterable<Place> places) {
        Iterable<Point> points = Iterables.transform(places, pointFunction());
        return Iterables.toArray(points, Point.class);
    }

    public static Function<Place, Point> pointFunction() {
        return new Function<Place, Point>() {
            @Override
            public Point apply(Place input) {
                return input.point();
            }
        };
    }

    /**
     * If the point was assigned to a Place this will provide the Place id or null otherwise.
     *
     * @param point
     * @return the Place id or null if the point wasn't associated with a destination.
     */
    public static Long id(Point point) {
        return (Long) point.getUserData();
    }

    public static Function<Place, String> nameFunction() {
        return new Function<Place, String>() {
            @Nullable
            @Override
            public String apply(Place input) {
                return (input != null) ? input.name() : null;
            }
        };
    }

    /**
     * Provides the predicate with the name given OR null if no match can be made. Places with the
     * same name will return one of the matches with no preference.
     * <p/>
     *
     * @param name
     * @param places
     * @return
     */
    @Nullable
    public static Place named(String name, Iterable<Place> places) {
        final Predicate<Place> predicate = Predicates.compose(Predicates.equalTo(name),
                nameFunction());
        final ImmutableListMultimap<String, Place> index = Multimaps.index(places, nameFunction());
        final String bestMatch = StringUtilsExtended.bestMatch(name, index.keys());
        return index.get(bestMatch).iterator().next();
    }
}
