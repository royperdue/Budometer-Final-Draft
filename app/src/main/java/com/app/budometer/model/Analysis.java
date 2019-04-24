package com.app.budometer.model;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;
import org.greenrobot.greendao.DaoException;


@Entity(nameInDb = "analysis")
public class Analysis {
    @Id(autoincrement = true)
    private Long analysisId;

    @Property(nameInDb = "saved")
    private boolean saved;

    @Property(nameInDb = "imagePath1")
    private String imagePath1;

    @Property(nameInDb = "imagePath2")
    private String imagePath2;

    @Property(nameInDb = "imagePath3")
    private String imagePath3;

    @Property(nameInDb = "imagePath4")
    private String imagePath4;

    @Property(nameInDb = "combinedImagePath")
    private String combinedImagePath;

    @Property(nameInDb = "combinedImageName")
    private String combinedImageName;

    @Property(nameInDb = "percentageTurnedPistils")
    private String percentageTurnedPistils;

    @Property(nameInDb = "date")
    private Long date;

    @Property(nameInDb = "cropId")
    private String cropId;

    @Property(nameInDb = "strain")
    private String strain;

    @Property(nameInDb = "notes")
    private String notes;

    @Property(nameInDb = "colorName")
    private String colorName;

    @Property(nameInDb = "lightRedPixelCount")
    private int lightRedPixelCount;

    @Property(nameInDb = "mediumRedPixelCount")
    private int mediumRedPixelCount;

    @Property(nameInDb = "redPixelCount")
    private int redPixelCount;

    @Property(nameInDb = "darkRedPixelCount")
    private int darkRedPixelCount;

    @Property(nameInDb = "lightPurplePixelCount")
    private int lightPurplePixelCount;

    @Property(nameInDb = "mediumPurplePixelCount")
    private int mediumPurplePixelCount;

    @Property(nameInDb = "purplePixelCount")
    private int purplePixelCount;

    @Property(nameInDb = "darkPurplePixelCount")
    private int darkPurplePixelCount;

    @Property(nameInDb = "lightGreenPixelCount")
    private int lightGreenPixelCount;

    @Property(nameInDb = "mediumGreenPixelCount")
    private int mediumGreenPixelCount;

    @Property(nameInDb = "greenPixelCount")
    private int greenPixelCount;

    @Property(nameInDb = "darkGreenPixelCount")
    private int darkGreenPixelCount;
    
    @Property(nameInDb = "lightYellowPixelCount")
    private int lightYellowPixelCount;

    @Property(nameInDb = "mediumYellowPixelCount")
    private int mediumYellowPixelCount;

    @Property(nameInDb = "yellowPixelCount")
    private int yellowPixelCount;

    @Property(nameInDb = "darkYellowPixelCount")
    private int darkYellowPixelCount;

    @Property(nameInDb = "lightOrangePixelCount")
    private int lightOrangePixelCount;

    @Property(nameInDb = "mediumOrangePixelCount")
    private int mediumOrangePixelCount;

    @Property(nameInDb = "orangePixelCount")
    private int orangePixelCount;

    @Property(nameInDb = "darkOrangePixelCount")
    private int darkOrangePixelCount;

    @Property(nameInDb = "lightBrownPixelCount")
    private int lightBrownPixelCount;

    @Property(nameInDb = "mediumBrownPixelCount")
    private int mediumBrownPixelCount;

    @Property(nameInDb = "brownPixelCount")
    private int brownPixelCount;

    @Property(nameInDb = "darkBrownPixelCount")
    private int darkBrownPixelCount;

    @Property(nameInDb = "lightGreyPixelCount")
    private int lightGreyPixelCount;

    @Property(nameInDb = "mediumGreyPixelCount")
    private int mediumGreyPixelCount;

    @Property(nameInDb = "greyPixelCount")
    private int greyPixelCount;

    @Property(nameInDb = "darkGreyPixelCount")
    private int darkGreyPixelCount;

    @Property(nameInDb = "lightRed")
    private int lightRed;

    @Property(nameInDb = "lightPurple")
    private int lightPurple;

    @Property(nameInDb = "lightGreen")
    private int lightGreen;

    @Property(nameInDb = "lightYellow")
    private int lightYellow;

    @Property(nameInDb = "lightOrange")
    private int lightOrange;

    @Property(nameInDb = "lightBrown")
    private int lightBrown;

    @Property(nameInDb = "lightGrey")
    private int lightGrey;

    @Property(nameInDb = "mediumRed")
    private int mediumRed;

    @Property(nameInDb = "mediumPurple")
    private int mediumPurple;

    @Property(nameInDb = "mediumGreen")
    private int mediumGreen;

    @Property(nameInDb = "mediumYellow")
    private int mediumYellow;

    @Property(nameInDb = "mediumOrange")
    private int mediumOrange;

    @Property(nameInDb = "mediumBrown")
    private int mediumBrown;

    @Property(nameInDb = "mediumGrey")
    private int mediumGrey;

    @Property(nameInDb = "red")
    private int red;

    @Property(nameInDb = "purple")
    private int purple;

    @Property(nameInDb = "green")
    private int green;

    @Property(nameInDb = "yellow")
    private int yellow;

    @Property(nameInDb = "orange")
    private int orange;

    @Property(nameInDb = "brown")
    private int brown;

    @Property(nameInDb = "grey")
    private int grey;

    @Property(nameInDb = "darkRed")
    private int darkRed;

    @Property(nameInDb = "darkPurple")
    private int darkPurple;

    @Property(nameInDb = "darkGreen")
    private int darkGreen;

    @Property(nameInDb = "darkYellow")
    private int darkYellow;

    @Property(nameInDb = "darkOrange")
    private int darkOrange;

    @Property(nameInDb = "darkBrown")
    private int darkBrown;

    @Property(nameInDb = "darkGrey")
    private int darkGrey;

    @Property(nameInDb = "tensorFlowTitleGrowing")
    private String tensorFlowTitleGrowing;

    @Property(nameInDb = "tensorFlowConfidenceGrowing")
    private float tensorFlowConfidenceGrowing;

    @Property(nameInDb = "tensorFlowTitleReady")
    private String tensorFlowTitleReady;

    @Property(nameInDb = "tensorFlowConfidenceReady")
    private float tensorFlowConfidenceReady;

    @Property(nameInDb = "tensorFlowConfidenceOrange")
    private float tensorFlowConfidenceOrange;

    @Property(nameInDb = "tensorFlowConfidencePurple")
    private float tensorFlowConfidencePurple;

    @Property(nameInDb = "tensorFlowConfidenceWhite")
    private float tensorFlowConfidenceWhite;

    @Property(nameInDb = "tensorFlowResult")
    private String tensorFlowResult;

    @Property(nameInDb = "totalPixelCount")
    private int totalPixelCount;

    @Generated(hash = 1304685431)
    public Analysis(Long analysisId, boolean saved, String imagePath1,
            String imagePath2, String imagePath3, String imagePath4,
            String combinedImagePath, String combinedImageName,
            String percentageTurnedPistils, Long date, String cropId, String strain,
            String notes, String colorName, int lightRedPixelCount,
            int mediumRedPixelCount, int redPixelCount, int darkRedPixelCount,
            int lightPurplePixelCount, int mediumPurplePixelCount,
            int purplePixelCount, int darkPurplePixelCount,
            int lightGreenPixelCount, int mediumGreenPixelCount,
            int greenPixelCount, int darkGreenPixelCount, int lightYellowPixelCount,
            int mediumYellowPixelCount, int yellowPixelCount,
            int darkYellowPixelCount, int lightOrangePixelCount,
            int mediumOrangePixelCount, int orangePixelCount,
            int darkOrangePixelCount, int lightBrownPixelCount,
            int mediumBrownPixelCount, int brownPixelCount, int darkBrownPixelCount,
            int lightGreyPixelCount, int mediumGreyPixelCount, int greyPixelCount,
            int darkGreyPixelCount, int lightRed, int lightPurple, int lightGreen,
            int lightYellow, int lightOrange, int lightBrown, int lightGrey,
            int mediumRed, int mediumPurple, int mediumGreen, int mediumYellow,
            int mediumOrange, int mediumBrown, int mediumGrey, int red, int purple,
            int green, int yellow, int orange, int brown, int grey, int darkRed,
            int darkPurple, int darkGreen, int darkYellow, int darkOrange,
            int darkBrown, int darkGrey, String tensorFlowTitleGrowing,
            float tensorFlowConfidenceGrowing, String tensorFlowTitleReady,
            float tensorFlowConfidenceReady, float tensorFlowConfidenceOrange,
            float tensorFlowConfidencePurple, float tensorFlowConfidenceWhite,
            String tensorFlowResult, int totalPixelCount) {
        this.analysisId = analysisId;
        this.saved = saved;
        this.imagePath1 = imagePath1;
        this.imagePath2 = imagePath2;
        this.imagePath3 = imagePath3;
        this.imagePath4 = imagePath4;
        this.combinedImagePath = combinedImagePath;
        this.combinedImageName = combinedImageName;
        this.percentageTurnedPistils = percentageTurnedPistils;
        this.date = date;
        this.cropId = cropId;
        this.strain = strain;
        this.notes = notes;
        this.colorName = colorName;
        this.lightRedPixelCount = lightRedPixelCount;
        this.mediumRedPixelCount = mediumRedPixelCount;
        this.redPixelCount = redPixelCount;
        this.darkRedPixelCount = darkRedPixelCount;
        this.lightPurplePixelCount = lightPurplePixelCount;
        this.mediumPurplePixelCount = mediumPurplePixelCount;
        this.purplePixelCount = purplePixelCount;
        this.darkPurplePixelCount = darkPurplePixelCount;
        this.lightGreenPixelCount = lightGreenPixelCount;
        this.mediumGreenPixelCount = mediumGreenPixelCount;
        this.greenPixelCount = greenPixelCount;
        this.darkGreenPixelCount = darkGreenPixelCount;
        this.lightYellowPixelCount = lightYellowPixelCount;
        this.mediumYellowPixelCount = mediumYellowPixelCount;
        this.yellowPixelCount = yellowPixelCount;
        this.darkYellowPixelCount = darkYellowPixelCount;
        this.lightOrangePixelCount = lightOrangePixelCount;
        this.mediumOrangePixelCount = mediumOrangePixelCount;
        this.orangePixelCount = orangePixelCount;
        this.darkOrangePixelCount = darkOrangePixelCount;
        this.lightBrownPixelCount = lightBrownPixelCount;
        this.mediumBrownPixelCount = mediumBrownPixelCount;
        this.brownPixelCount = brownPixelCount;
        this.darkBrownPixelCount = darkBrownPixelCount;
        this.lightGreyPixelCount = lightGreyPixelCount;
        this.mediumGreyPixelCount = mediumGreyPixelCount;
        this.greyPixelCount = greyPixelCount;
        this.darkGreyPixelCount = darkGreyPixelCount;
        this.lightRed = lightRed;
        this.lightPurple = lightPurple;
        this.lightGreen = lightGreen;
        this.lightYellow = lightYellow;
        this.lightOrange = lightOrange;
        this.lightBrown = lightBrown;
        this.lightGrey = lightGrey;
        this.mediumRed = mediumRed;
        this.mediumPurple = mediumPurple;
        this.mediumGreen = mediumGreen;
        this.mediumYellow = mediumYellow;
        this.mediumOrange = mediumOrange;
        this.mediumBrown = mediumBrown;
        this.mediumGrey = mediumGrey;
        this.red = red;
        this.purple = purple;
        this.green = green;
        this.yellow = yellow;
        this.orange = orange;
        this.brown = brown;
        this.grey = grey;
        this.darkRed = darkRed;
        this.darkPurple = darkPurple;
        this.darkGreen = darkGreen;
        this.darkYellow = darkYellow;
        this.darkOrange = darkOrange;
        this.darkBrown = darkBrown;
        this.darkGrey = darkGrey;
        this.tensorFlowTitleGrowing = tensorFlowTitleGrowing;
        this.tensorFlowConfidenceGrowing = tensorFlowConfidenceGrowing;
        this.tensorFlowTitleReady = tensorFlowTitleReady;
        this.tensorFlowConfidenceReady = tensorFlowConfidenceReady;
        this.tensorFlowConfidenceOrange = tensorFlowConfidenceOrange;
        this.tensorFlowConfidencePurple = tensorFlowConfidencePurple;
        this.tensorFlowConfidenceWhite = tensorFlowConfidenceWhite;
        this.tensorFlowResult = tensorFlowResult;
        this.totalPixelCount = totalPixelCount;
    }

    @Generated(hash = 915320899)
    public Analysis() {
    }

    public Long getAnalysisId() {
        return this.analysisId;
    }

    public void setAnalysisId(Long analysisId) {
        this.analysisId = analysisId;
    }

    public boolean getSaved() {
        return this.saved;
    }

    public void setSaved(boolean saved) {
        this.saved = saved;
    }

    public String getImagePath1() {
        return this.imagePath1;
    }

    public void setImagePath1(String imagePath1) {
        this.imagePath1 = imagePath1;
    }

    public String getImagePath2() {
        return this.imagePath2;
    }

    public void setImagePath2(String imagePath2) {
        this.imagePath2 = imagePath2;
    }

    public String getImagePath3() {
        return this.imagePath3;
    }

    public void setImagePath3(String imagePath3) {
        this.imagePath3 = imagePath3;
    }

    public String getImagePath4() {
        return this.imagePath4;
    }

    public void setImagePath4(String imagePath4) {
        this.imagePath4 = imagePath4;
    }

    public String getCombinedImagePath() {
        return this.combinedImagePath;
    }

    public void setCombinedImagePath(String combinedImagePath) {
        this.combinedImagePath = combinedImagePath;
    }

    public String getCombinedImageName() {
        return this.combinedImageName;
    }

    public void setCombinedImageName(String combinedImageName) {
        this.combinedImageName = combinedImageName;
    }

    public String getPercentageTurnedPistils() {
        return this.percentageTurnedPistils;
    }

    public void setPercentageTurnedPistils(String percentageTurnedPistils) {
        this.percentageTurnedPistils = percentageTurnedPistils;
    }

    public Long getDate() {
        return this.date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public String getCropId() {
        return this.cropId;
    }

    public void setCropId(String cropId) {
        this.cropId = cropId;
    }

    public String getStrain() {
        return this.strain;
    }

    public void setStrain(String strain) {
        this.strain = strain;
    }

    public String getNotes() {
        return this.notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getColorName() {
        return this.colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    public int getLightRedPixelCount() {
        return this.lightRedPixelCount;
    }

    public void setLightRedPixelCount(int lightRedPixelCount) {
        this.lightRedPixelCount = lightRedPixelCount;
    }

    public int getMediumRedPixelCount() {
        return this.mediumRedPixelCount;
    }

    public void setMediumRedPixelCount(int mediumRedPixelCount) {
        this.mediumRedPixelCount = mediumRedPixelCount;
    }

    public int getRedPixelCount() {
        return this.redPixelCount;
    }

    public void setRedPixelCount(int redPixelCount) {
        this.redPixelCount = redPixelCount;
    }

    public int getDarkRedPixelCount() {
        return this.darkRedPixelCount;
    }

    public void setDarkRedPixelCount(int darkRedPixelCount) {
        this.darkRedPixelCount = darkRedPixelCount;
    }

    public int getLightPurplePixelCount() {
        return this.lightPurplePixelCount;
    }

    public void setLightPurplePixelCount(int lightPurplePixelCount) {
        this.lightPurplePixelCount = lightPurplePixelCount;
    }

    public int getMediumPurplePixelCount() {
        return this.mediumPurplePixelCount;
    }

    public void setMediumPurplePixelCount(int mediumPurplePixelCount) {
        this.mediumPurplePixelCount = mediumPurplePixelCount;
    }

    public int getPurplePixelCount() {
        return this.purplePixelCount;
    }

    public void setPurplePixelCount(int purplePixelCount) {
        this.purplePixelCount = purplePixelCount;
    }

    public int getDarkPurplePixelCount() {
        return this.darkPurplePixelCount;
    }

    public void setDarkPurplePixelCount(int darkPurplePixelCount) {
        this.darkPurplePixelCount = darkPurplePixelCount;
    }

    public int getLightGreenPixelCount() {
        return this.lightGreenPixelCount;
    }

    public void setLightGreenPixelCount(int lightGreenPixelCount) {
        this.lightGreenPixelCount = lightGreenPixelCount;
    }

    public int getMediumGreenPixelCount() {
        return this.mediumGreenPixelCount;
    }

    public void setMediumGreenPixelCount(int mediumGreenPixelCount) {
        this.mediumGreenPixelCount = mediumGreenPixelCount;
    }

    public int getGreenPixelCount() {
        return this.greenPixelCount;
    }

    public void setGreenPixelCount(int greenPixelCount) {
        this.greenPixelCount = greenPixelCount;
    }

    public int getDarkGreenPixelCount() {
        return this.darkGreenPixelCount;
    }

    public void setDarkGreenPixelCount(int darkGreenPixelCount) {
        this.darkGreenPixelCount = darkGreenPixelCount;
    }

    public int getLightYellowPixelCount() {
        return this.lightYellowPixelCount;
    }

    public void setLightYellowPixelCount(int lightYellowPixelCount) {
        this.lightYellowPixelCount = lightYellowPixelCount;
    }

    public int getMediumYellowPixelCount() {
        return this.mediumYellowPixelCount;
    }

    public void setMediumYellowPixelCount(int mediumYellowPixelCount) {
        this.mediumYellowPixelCount = mediumYellowPixelCount;
    }

    public int getYellowPixelCount() {
        return this.yellowPixelCount;
    }

    public void setYellowPixelCount(int yellowPixelCount) {
        this.yellowPixelCount = yellowPixelCount;
    }

    public int getDarkYellowPixelCount() {
        return this.darkYellowPixelCount;
    }

    public void setDarkYellowPixelCount(int darkYellowPixelCount) {
        this.darkYellowPixelCount = darkYellowPixelCount;
    }

    public int getLightOrangePixelCount() {
        return this.lightOrangePixelCount;
    }

    public void setLightOrangePixelCount(int lightOrangePixelCount) {
        this.lightOrangePixelCount = lightOrangePixelCount;
    }

    public int getMediumOrangePixelCount() {
        return this.mediumOrangePixelCount;
    }

    public void setMediumOrangePixelCount(int mediumOrangePixelCount) {
        this.mediumOrangePixelCount = mediumOrangePixelCount;
    }

    public int getOrangePixelCount() {
        return this.orangePixelCount;
    }

    public void setOrangePixelCount(int orangePixelCount) {
        this.orangePixelCount = orangePixelCount;
    }

    public int getDarkOrangePixelCount() {
        return this.darkOrangePixelCount;
    }

    public void setDarkOrangePixelCount(int darkOrangePixelCount) {
        this.darkOrangePixelCount = darkOrangePixelCount;
    }

    public int getLightBrownPixelCount() {
        return this.lightBrownPixelCount;
    }

    public void setLightBrownPixelCount(int lightBrownPixelCount) {
        this.lightBrownPixelCount = lightBrownPixelCount;
    }

    public int getMediumBrownPixelCount() {
        return this.mediumBrownPixelCount;
    }

    public void setMediumBrownPixelCount(int mediumBrownPixelCount) {
        this.mediumBrownPixelCount = mediumBrownPixelCount;
    }

    public int getBrownPixelCount() {
        return this.brownPixelCount;
    }

    public void setBrownPixelCount(int brownPixelCount) {
        this.brownPixelCount = brownPixelCount;
    }

    public int getDarkBrownPixelCount() {
        return this.darkBrownPixelCount;
    }

    public void setDarkBrownPixelCount(int darkBrownPixelCount) {
        this.darkBrownPixelCount = darkBrownPixelCount;
    }

    public int getLightGreyPixelCount() {
        return this.lightGreyPixelCount;
    }

    public void setLightGreyPixelCount(int lightGreyPixelCount) {
        this.lightGreyPixelCount = lightGreyPixelCount;
    }

    public int getMediumGreyPixelCount() {
        return this.mediumGreyPixelCount;
    }

    public void setMediumGreyPixelCount(int mediumGreyPixelCount) {
        this.mediumGreyPixelCount = mediumGreyPixelCount;
    }

    public int getGreyPixelCount() {
        return this.greyPixelCount;
    }

    public void setGreyPixelCount(int greyPixelCount) {
        this.greyPixelCount = greyPixelCount;
    }

    public int getDarkGreyPixelCount() {
        return this.darkGreyPixelCount;
    }

    public void setDarkGreyPixelCount(int darkGreyPixelCount) {
        this.darkGreyPixelCount = darkGreyPixelCount;
    }

    public int getLightRed() {
        return this.lightRed;
    }

    public void setLightRed(int lightRed) {
        this.lightRed = lightRed;
    }

    public int getLightPurple() {
        return this.lightPurple;
    }

    public void setLightPurple(int lightPurple) {
        this.lightPurple = lightPurple;
    }

    public int getLightGreen() {
        return this.lightGreen;
    }

    public void setLightGreen(int lightGreen) {
        this.lightGreen = lightGreen;
    }

    public int getLightYellow() {
        return this.lightYellow;
    }

    public void setLightYellow(int lightYellow) {
        this.lightYellow = lightYellow;
    }

    public int getLightOrange() {
        return this.lightOrange;
    }

    public void setLightOrange(int lightOrange) {
        this.lightOrange = lightOrange;
    }

    public int getLightBrown() {
        return this.lightBrown;
    }

    public void setLightBrown(int lightBrown) {
        this.lightBrown = lightBrown;
    }

    public int getLightGrey() {
        return this.lightGrey;
    }

    public void setLightGrey(int lightGrey) {
        this.lightGrey = lightGrey;
    }

    public int getMediumRed() {
        return this.mediumRed;
    }

    public void setMediumRed(int mediumRed) {
        this.mediumRed = mediumRed;
    }

    public int getMediumPurple() {
        return this.mediumPurple;
    }

    public void setMediumPurple(int mediumPurple) {
        this.mediumPurple = mediumPurple;
    }

    public int getMediumGreen() {
        return this.mediumGreen;
    }

    public void setMediumGreen(int mediumGreen) {
        this.mediumGreen = mediumGreen;
    }

    public int getMediumYellow() {
        return this.mediumYellow;
    }

    public void setMediumYellow(int mediumYellow) {
        this.mediumYellow = mediumYellow;
    }

    public int getMediumOrange() {
        return this.mediumOrange;
    }

    public void setMediumOrange(int mediumOrange) {
        this.mediumOrange = mediumOrange;
    }

    public int getMediumBrown() {
        return this.mediumBrown;
    }

    public void setMediumBrown(int mediumBrown) {
        this.mediumBrown = mediumBrown;
    }

    public int getMediumGrey() {
        return this.mediumGrey;
    }

    public void setMediumGrey(int mediumGrey) {
        this.mediumGrey = mediumGrey;
    }

    public int getRed() {
        return this.red;
    }

    public void setRed(int red) {
        this.red = red;
    }

    public int getPurple() {
        return this.purple;
    }

    public void setPurple(int purple) {
        this.purple = purple;
    }

    public int getGreen() {
        return this.green;
    }

    public void setGreen(int green) {
        this.green = green;
    }

    public int getYellow() {
        return this.yellow;
    }

    public void setYellow(int yellow) {
        this.yellow = yellow;
    }

    public int getOrange() {
        return this.orange;
    }

    public void setOrange(int orange) {
        this.orange = orange;
    }

    public int getBrown() {
        return this.brown;
    }

    public void setBrown(int brown) {
        this.brown = brown;
    }

    public int getGrey() {
        return this.grey;
    }

    public void setGrey(int grey) {
        this.grey = grey;
    }

    public int getDarkRed() {
        return this.darkRed;
    }

    public void setDarkRed(int darkRed) {
        this.darkRed = darkRed;
    }

    public int getDarkPurple() {
        return this.darkPurple;
    }

    public void setDarkPurple(int darkPurple) {
        this.darkPurple = darkPurple;
    }

    public int getDarkGreen() {
        return this.darkGreen;
    }

    public void setDarkGreen(int darkGreen) {
        this.darkGreen = darkGreen;
    }

    public int getDarkYellow() {
        return this.darkYellow;
    }

    public void setDarkYellow(int darkYellow) {
        this.darkYellow = darkYellow;
    }

    public int getDarkOrange() {
        return this.darkOrange;
    }

    public void setDarkOrange(int darkOrange) {
        this.darkOrange = darkOrange;
    }

    public int getDarkBrown() {
        return this.darkBrown;
    }

    public void setDarkBrown(int darkBrown) {
        this.darkBrown = darkBrown;
    }

    public int getDarkGrey() {
        return this.darkGrey;
    }

    public void setDarkGrey(int darkGrey) {
        this.darkGrey = darkGrey;
    }

    public String getTensorFlowTitleGrowing() {
        return this.tensorFlowTitleGrowing;
    }

    public void setTensorFlowTitleGrowing(String tensorFlowTitleGrowing) {
        this.tensorFlowTitleGrowing = tensorFlowTitleGrowing;
    }

    public float getTensorFlowConfidenceGrowing() {
        return this.tensorFlowConfidenceGrowing;
    }

    public void setTensorFlowConfidenceGrowing(float tensorFlowConfidenceGrowing) {
        this.tensorFlowConfidenceGrowing = tensorFlowConfidenceGrowing;
    }

    public String getTensorFlowTitleReady() {
        return this.tensorFlowTitleReady;
    }

    public void setTensorFlowTitleReady(String tensorFlowTitleReady) {
        this.tensorFlowTitleReady = tensorFlowTitleReady;
    }

    public float getTensorFlowConfidenceReady() {
        return this.tensorFlowConfidenceReady;
    }

    public void setTensorFlowConfidenceReady(float tensorFlowConfidenceReady) {
        this.tensorFlowConfidenceReady = tensorFlowConfidenceReady;
    }

    public float getTensorFlowConfidenceOrange() {
        return this.tensorFlowConfidenceOrange;
    }

    public void setTensorFlowConfidenceOrange(float tensorFlowConfidenceOrange) {
        this.tensorFlowConfidenceOrange = tensorFlowConfidenceOrange;
    }

    public float getTensorFlowConfidencePurple() {
        return this.tensorFlowConfidencePurple;
    }

    public void setTensorFlowConfidencePurple(float tensorFlowConfidencePurple) {
        this.tensorFlowConfidencePurple = tensorFlowConfidencePurple;
    }

    public float getTensorFlowConfidenceWhite() {
        return this.tensorFlowConfidenceWhite;
    }

    public void setTensorFlowConfidenceWhite(float tensorFlowConfidenceWhite) {
        this.tensorFlowConfidenceWhite = tensorFlowConfidenceWhite;
    }

    public String getTensorFlowResult() {
        return this.tensorFlowResult;
    }

    public void setTensorFlowResult(String tensorFlowResult) {
        this.tensorFlowResult = tensorFlowResult;
    }

    public int getTotalPixelCount() {
        return this.totalPixelCount;
    }

    public void setTotalPixelCount(int totalPixelCount) {
        this.totalPixelCount = totalPixelCount;
    }
}
