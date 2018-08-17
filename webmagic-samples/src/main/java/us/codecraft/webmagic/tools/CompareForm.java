package us.codecraft.webmagic.tools;

public class CompareForm {

    private String OrgName;
    private String m1Name;
    private String m1ProductID;
    private String m2Name;
    private String m2ProductID;
    private Float similarityScoreM1;
    private Float similarityScoreM2;
    private String flatM;

    public String getOrgName() {
        return OrgName;
    }

    public void setOrgName(String orgName) {
        OrgName = orgName;
    }

    public String getM1Name() {
        return m1Name;
    }

    public void setM1Name(String m1Name) {
        this.m1Name = m1Name;
    }

    public String getM1ProductID() {
        return m1ProductID;
    }

    public void setM1ProductID(String m1ProductID) {
        this.m1ProductID = m1ProductID;
    }

    public String getM2Name() {
        return m2Name;
    }

    public void setM2Name(String m2Name) {
        this.m2Name = m2Name;
    }

    public String getM2ProductID() {
        return m2ProductID;
    }

    public void setM2ProductID(String m2ProductID) {
        this.m2ProductID = m2ProductID;
    }

    public Float getSimilarityScoreM1() {
        return similarityScoreM1;
    }

    public void setSimilarityScoreM1(Float similarityScoreM1) {
        this.similarityScoreM1 = similarityScoreM1;
    }

    public Float getSimilarityScoreM2() {
        return similarityScoreM2;
    }

    public void setSimilarityScoreM2(Float similarityScoreM2) {
        this.similarityScoreM2 = similarityScoreM2;
    }

    public String getFlatM() {
        return flatM;
    }

    public void setFlatM(String flatM) {
        this.flatM = flatM;
    }
}
