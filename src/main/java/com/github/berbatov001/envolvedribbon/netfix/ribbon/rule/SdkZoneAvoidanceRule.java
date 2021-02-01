package com.github.berbatov001.envolvedribbon.netfix.ribbon.rule;

import com.github.berbatov001.envolvedribbon.netfix.ribbon.predicate.SdkGrayEnabledPredicate;
import com.netflix.loadbalancer.*;

public class SdkZoneAvoidanceRule extends ZoneAvoidanceRule {

    private CompositePredicate compositePredicate;

    public void setPredicate() {
        ZoneAvoidancePredicate zoneAvoidancePredicate = new ZoneAvoidancePredicate(this, null);
        AvailabilityPredicate availabilityPredicate = new AvailabilityPredicate(this, null);
        SdkGrayEnabledPredicate sdkZoneAvoidanceRule = new SdkGrayEnabledPredicate(this);
        this.compositePredicate = CompositePredicate.withPredicates(zoneAvoidancePredicate, availabilityPredicate, sdkZoneAvoidanceRule).build();
    }

    @Override
    public AbstractServerPredicate getPredicate() {
        return compositePredicate;
    }
}
