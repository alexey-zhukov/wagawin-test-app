package com.wagawin.testapp.service;

import com.wagawin.testapp.dto.ParentSummaryDto;
import com.wagawin.testapp.entity.ParentSummary;
import com.wagawin.testapp.repository.ChildRepository;
import com.wagawin.testapp.repository.ParentSummaryRepository;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class ParentSummaryService {
    private ChildRepository childRepository;
    private ParentSummaryRepository parentSummaryRepository;
    private Integer[] summary;
    private final Object summaryLock = new Object();

    public ParentSummaryService(ChildRepository childRepository,
                                ParentSummaryRepository parentSummaryRepository) {
        this.childRepository = childRepository;
        this.parentSummaryRepository = parentSummaryRepository;
    }

    @Scheduled(fixedDelay = 15, timeUnit = TimeUnit.MINUTES)
    @Transactional public void recalcParentSummary() {
        List<ParentSummary> collect = this.childRepository.calcParentSummary()
                .stream().map(ParentSummary::new).collect(Collectors.toList());
        parentSummaryRepository.deleteAll();
        parentSummaryRepository.saveAll(collect);
        // next /summary call will renew this.summary
        this.summary = null;
    }

    public ParentSummaryDto summary() {
        if (this.summary != null)
            return new ParentSummaryDto(summary);
        else {
            synchronized (summaryLock) {
                if (summary != null)
                    return new ParentSummaryDto(this.summary);

                List<ParentSummary> amountOfChildren = this.parentSummaryRepository
                        .findAll(Sort.by("amountOfChildren"));
                if (amountOfChildren.isEmpty())
                    return new ParentSummaryDto(this.summary = new Integer[0]);
                else {
                    Integer[] result = new Integer[amountOfChildren.get(
                            amountOfChildren.size() - 1).getAmountOfChildren()];
                    for (int nI = 1; nI <= amountOfChildren.get(
                            amountOfChildren.size() - 1).getAmountOfChildren(); ++nI) {
                        for (ParentSummary anAmountOfChildren : amountOfChildren) {
                            if (anAmountOfChildren.getAmountOfChildren() == nI) {
                                result[nI - 1] = anAmountOfChildren.getAmountOfPersons();
                                break;
                            }
                        }
                    }
                    return new ParentSummaryDto(this.summary = result);
                }
            }
        }
    }
}
