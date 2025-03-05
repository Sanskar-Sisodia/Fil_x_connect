package com.filxconnect.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.filxconnect.dto.PostReportDTO;
import com.filxconnect.dto.UserReportDTO;
import com.filxconnect.entity.Report;
import com.filxconnect.entity.ReportStatus;
import com.filxconnect.repository.ReportRepository;
import com.filxconnect.repository.UserRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ReportService {
    @Autowired
    private ReportRepository reportRepository;
    private UserService userService;

    // ✅ Report a User
    public Report reportUser(UserReportDTO userReportDTO) {
        Report report = new Report();
        report.setReporterUserId(userReportDTO.getReporterUserId());
        report.setReportedUserId(userReportDTO.getReportedUserId());
        report.setReason(userReportDTO.getReason());
        reportRepository.save(report);
        userService.incReports(userReportDTO.getReportedUserId());
        return report;
    }

    // ✅ Report a Post
    public Report reportPost(PostReportDTO postReportDTO) {
        Report report = new Report();
        report.setReporterUserId(postReportDTO.getReporterUserId());
        report.setReportedPostId(postReportDTO.getReportedPostId());
        report.setReason(postReportDTO.getReason());
        reportRepository.save(report);
        return report;
    }

    // ✅ Get reported users
    public List<Report> getReportedUsers() {
        return reportRepository.findAll().stream()
                .filter(report -> report.getReportedUserId() != null)
                .collect(Collectors.toList());
    }
    
    public long countReportedUsers() {
    	return reportRepository.countDistinctReportedUsers();
    }
    
    public List<Report> getAllReports(){
    	return reportRepository.findAll();
    }

    // ✅ Get reported posts
    public List<Report> getReportedPosts() {
        return reportRepository.findAll().stream()
                .filter(report -> report.getReportedPostId() != null)
                .collect(Collectors.toList());
    }

    // ✅ Update Report Status
    public String updateReportStatus(UUID userId, ReportStatus status) {
        List<Report> reports = reportRepository.findByReportedUserId(userId);
        reports.forEach(report -> report.setReportStatus(status));
        reportRepository.saveAll(reports);
        return "Report status updated successfully";
    }
    public String updatePostReportStatus(UUID postId, String status) {
        List<Report> reports = reportRepository.findByReportedPostId(postId);
        
        if (reports.isEmpty()) {
            return "No reports found for this post.";
        }

        for (Report report : reports) {
            report.setReportStatus(ReportStatus.valueOf(status.toUpperCase()));
        }

        reportRepository.saveAll(reports);
        return "Post report status updated successfully";
    }
}
