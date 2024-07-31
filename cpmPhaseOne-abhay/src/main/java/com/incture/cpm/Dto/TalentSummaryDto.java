package com.incture.cpm.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TalentSummaryDto {
    private long totalTalents;
    private long activeTalents;
    private long inactiveTalents;
    private long declinedTalents;
    private long resignedTalents;
    private long revokedTalents;
    private long talentLeftForBetterOffer;
    private long talentLeftForHigherStudies;
    private long talentLeftForFamilyReasons;
    private long talentLeftForHealthReasons;
    private long talentLeftForPerformanceIssues;
    private long talentLeftForOthers;
}
