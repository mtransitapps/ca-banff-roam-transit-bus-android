package org.mtransit.parser.ca_banff_roam_transit_bus;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mtransit.commons.CleanUtils;
import org.mtransit.commons.Constants;
import org.mtransit.parser.DefaultAgencyTools;
import org.mtransit.parser.mt.data.MAgency;

import java.util.regex.Pattern;

// http://www.banffopendata.ca/
// https://roamtransit.com/wp-content/uploads/GTFS/GTFS.zip
// http://data.trilliumtransit.com/gtfs/roamtransit-banff-ab-ca/roamtransit-banff-ab-ca.zip
public class BanffRoamTransitBusAgencyTools extends DefaultAgencyTools {

	public static void main(@NotNull String[] args) {
		new BanffRoamTransitBusAgencyTools().start(args);
	}

	@NotNull
	@Override
	public String getAgencyName() {
		return "Roam Transit";
	}

	@Override
	public boolean defaultExcludeEnabled() {
		return true;
	}

	@NotNull
	@Override
	public Integer getAgencyRouteType() {
		return MAgency.ROUTE_TYPE_BUS;
	}

	@Override
	public boolean defaultRouteIdEnabled() {
		return true;
	}

	@Override
	public boolean useRouteShortNameForRouteId() {
		return true;
	}

	@Nullable
	@Override
	public Long convertRouteIdFromShortNameNotSupported(@NotNull String routeShortName) {
		switch (routeShortName) {
		case "On-It":
			return 1000L;
		default:
			return super.convertRouteIdFromShortNameNotSupported(routeShortName);
		}
	}

	private static final Pattern STARTS_WITH_ROUTE_RID = Pattern.compile("(route [0-9]+[a-z]?( & [0-9]+)? (- )?)", Pattern.CASE_INSENSITIVE);

	@NotNull
	@Override
	public String cleanRouteLongName(@NotNull String routeLongName) {
		routeLongName = STARTS_WITH_ROUTE_RID.matcher(routeLongName).replaceAll(Constants.EMPTY);
		routeLongName = CleanUtils.CLEAN_AND.matcher(routeLongName).replaceAll(CleanUtils.CLEAN_AND_REPLACEMENT);
		routeLongName = CleanUtils.cleanStreetTypes(routeLongName);
		return CleanUtils.cleanLabel(routeLongName);
	}

	@Override
	public boolean defaultAgencyColorEnabled() {
		return true;
	}

	private static final String AGENCY_COLOR_DARK_GREY = "231F20"; // DARK GREY (from PNG logo)

	private static final String AGENCY_COLOR = AGENCY_COLOR_DARK_GREY;

	@NotNull
	@Override
	public String getAgencyColor() {
		return AGENCY_COLOR;
	}

	@Override
	public boolean directionFinderEnabled() {
		return true;
	}

	@NotNull
	@Override
	public String cleanTripHeadsign(@NotNull String tripHeadsign) {
		tripHeadsign = CleanUtils.CLEAN_AND.matcher(tripHeadsign).replaceAll(CleanUtils.CLEAN_AND_REPLACEMENT);
		tripHeadsign = CleanUtils.cleanStreetTypes(tripHeadsign);
		return CleanUtils.cleanLabel(tripHeadsign);
	}

	private static final Pattern HIGH_SCHOOL_ = CleanUtils.cleanWords("high school");
	private static final String HIGH_SCHOOL_REPLACEMENT = CleanUtils.cleanWordsReplacement("HS");

	private static final Pattern TRANSIT_HUB_ = CleanUtils.cleanWords("transit hub");

	@NotNull
	@Override
	public String cleanDirectionHeadsign(int directionId, boolean fromStopName, @NotNull String directionHeadSign) {
		directionHeadSign = HIGH_SCHOOL_.matcher(directionHeadSign).replaceAll(HIGH_SCHOOL_REPLACEMENT);
		directionHeadSign = TRANSIT_HUB_.matcher(directionHeadSign).replaceAll(Constants.EMPTY);
		return super.cleanDirectionHeadsign(directionId, fromStopName, directionHeadSign);
	}

	@NotNull
	@Override
	public String cleanStopName(@NotNull String gStopName) {
		gStopName = CleanUtils.CLEAN_AND.matcher(gStopName).replaceAll(CleanUtils.CLEAN_AND_REPLACEMENT);
		gStopName = CleanUtils.cleanNumbers(gStopName);
		gStopName = CleanUtils.cleanStreetTypes(gStopName);
		return CleanUtils.cleanLabel(gStopName);
	}
}
