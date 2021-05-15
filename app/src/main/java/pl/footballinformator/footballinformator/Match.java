package pl.footballinformator.footballinformator;

public class Match {


    // @param date of match
    private String mDate;

    // @param home team
    private String mHomeTeam;

    // @param away team
    private String mAwayTeam;

    // @param home team score
    private Integer mHomeScore;

    // @param away team score
    private Integer mAwayScore;

    // @param home logo ID
    private Integer mHomeLogoID = NO_IMAGE_PROVIDED;

    // @param away logo ID
    private Integer mAwayLogoID = NO_IMAGE_PROVIDED;

    private String mStatus;

    private static final int NO_IMAGE_PROVIDED = -1;


    public Match(String date, String homeTeam, String awayTeam, Integer homeScore, Integer awayScore, int homeLogoID, int awayLogoID, String status) {
        mDate = date;
        mHomeTeam = homeTeam;
        mAwayTeam = awayTeam;
        mHomeScore = homeScore;
        mAwayScore = awayScore;
        mHomeLogoID = homeLogoID;
        mAwayLogoID = awayLogoID;
        mStatus = status;
    }

    public String getDate() {
        return mDate;
    }

    public String getHomeTeam() {
        return mHomeTeam;
    }

    public String getAwayTeam() {
        return mAwayTeam;
    }

    public Integer getHomeScore() {
        return mHomeScore;
    }

    public Integer getAwayScore() {
        return mAwayScore;
    }

    public boolean hasHomeLogo() {
        return mHomeLogoID != NO_IMAGE_PROVIDED;
    }

    public boolean hasAwayLogo() {
        return mAwayLogoID != NO_IMAGE_PROVIDED;
    }

    public Integer getHomeLogoID() {
        return mHomeLogoID;
    }

    public Integer getAwayLogoID() {
        return mAwayLogoID;
    }

    public String getStatus() {
        return mStatus;
    }
}
