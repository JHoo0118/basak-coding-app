package com.kosmo.basakcoding.models;

import java.util.ArrayList;
import java.util.List;

public class CurriculumDTO {
    public int curriculumId;
    public int courseId;
    public int courseTitle;
    public String name;
    public List<VideoDTO> videos = new ArrayList<VideoDTO>();

    public int getCurriculumId() {
        return curriculumId;
    }

    public void setCurriculumId(int curriculumId) {
        this.curriculumId = curriculumId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(int courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<VideoDTO> getVideos() {
        return videos;
    }

    public void setVideos(List<VideoDTO> videos) {
        this.videos = videos;
    }
}
