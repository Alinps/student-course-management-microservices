package com.example.course_service.service;

import com.example.course_service.dto.TechnologyDayPatchRequest;
import com.example.course_service.dto.TechnologyDayRequest;
import com.example.course_service.dto.TechnologyDayResponse;
import com.example.course_service.exception.IncorrectTechnologyException;
import com.example.course_service.exception.ResourceAlreadyExistException;
import com.example.course_service.exception.ResourceNotFoundException;
import com.example.course_service.mapper.TechnologyDayMapper;
import com.example.course_service.models.Technology;
import com.example.course_service.models.TechnologyDay;
import com.example.course_service.repository.TechnologyDayRepository;
import com.example.course_service.repository.TechnologyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TechnologyDayServiceImpl implements  TechnologyDayService {

    private final TechnologyDayRepository technologyDayRepository;
    private final TechnologyDayMapper mapper;
    private final TechnologyRepository technologyRepository;
    private static final Logger log = LoggerFactory.getLogger(TechnologyDayServiceImpl.class);

    public TechnologyDayServiceImpl (
            TechnologyDayRepository technologyDayRepository,
            TechnologyRepository technologyRepository,
            TechnologyDayMapper mapper) {
        this.technologyDayRepository = technologyDayRepository;
        this.mapper = mapper;
        this.technologyRepository = technologyRepository;
    }

    @Override
    public TechnologyDayResponse createDaySplit(Long technologyId, TechnologyDayRequest request) {

        log.info(
                "Creating technology day split. technologyId={}, dayNumber={}",
                technologyId,
                request.getDayNumber()
        );

        Technology technology = technologyRepository.findById(technologyId)
                .orElseThrow(() -> {

                    log.warn(
                            "Technology day split creation failed. " +
                                    "Technology not found. technologyId={}",
                            technologyId
                    );

                    return new IncorrectTechnologyException("Technology id is not correct: " + technologyId);
                });

        if (technologyDayRepository.existsByTechnologyIdAndDayNumber(technologyId, request.getDayNumber())) {

            log.warn(
                    "Technology day split creation rejected. " +
                            "Day split already exists. technologyId={}, dayNumber={}",
                    technologyId,
                    request.getDayNumber()
            );
            throw new ResourceAlreadyExistException("Day Split already exists.");
        }

        TechnologyDay technologyDay = mapper.toEntity(request);
        technologyDay.setTechnology(technology);
        TechnologyDay savedTechnologyDay = technologyDayRepository.save(technologyDay);

        log.info(
                "Technology day split created successfully. " +
                        "technologyDayId={}, technologyId={}, dayNumber={}",
                savedTechnologyDay.getId(),
                technologyId,
                savedTechnologyDay.getDayNumber()
        );

        return mapper.toResponseDTO(savedTechnologyDay);

    }



    @Override
    public List<TechnologyDayResponse> getDaySplitByTechnologyId(Long technologyId) {

        List<TechnologyDay> technologyDays = technologyDayRepository.findByTechnologyId(technologyId);

        if (technologyDays.isEmpty()) {

            log.warn("Failed to get Techonology day split. Technology not found. technologyId={}", technologyId);
            throw new ResourceNotFoundException("No Technology Days found.");

        }

        return  mapper.toResponseDTOList(technologyDays);
    }



    @Override
    public List<TechnologyDayResponse> getAllDaySplit() {

        List<TechnologyDay> technologyDays = technologyDayRepository.findAll();

        return mapper.toResponseDTOList(technologyDays);

    }

    @Override
    public TechnologyDayResponse getDaySplitById(Long id) {

        TechnologyDay technologyDay = technologyDayRepository.findById(id)
                .orElseThrow(() -> {

                    log.warn(
                            "Failed to get technology day split. " +
                                    "Technology day split not found. technologyDayId={}",
                            id
                    );

                    return new ResourceNotFoundException(
                            "Technology Day split not found"
                    );
                });

        return mapper.toResponseDTO(technologyDay);
    }



    @Override
    public TechnologyDayResponse updateDaySplit(Long id, Long technologyId, TechnologyDayRequest request) {


        log.info(
                "Updating technology day split. technologyDayId={}, technologyId={}, dayNumber={}",
                id,
                technologyId,
                request.getDayNumber()
        );

        Technology technology = technologyRepository.findById(technologyId)
                .orElseThrow(() -> {

                    log.warn(
                            "Technology day split update failed. " +
                                    "Technology not found. technologyId={}",
                            technologyId
                    );

                    return new IncorrectTechnologyException("Technology id is not correct: " + technologyId);
                });


        TechnologyDay technologyDay = technologyDayRepository.findById(id)
                .orElseThrow(() -> {

                    log.warn(
                            "Technology day split update failed. " +
                                    "Technology day split not found. technologyDayId={}",
                            id
                    );

                    return new ResourceNotFoundException("Technology Day split not found");
                });

        technologyDay.setTechnology(technology);

        mapper.updateEntity(request, technologyDay);

        TechnologyDay updatedTechnologyDay = technologyDayRepository.save(technologyDay);

        log.info(
                "Technology day split updated successfully. " +
                        "technologyDayId={}, technologyId={}, dayNumber={}",
                updatedTechnologyDay.getId(),
                updatedTechnologyDay.getTechnology().getId(),
                updatedTechnologyDay.getDayNumber()
        );


        return mapper.toResponseDTO(updatedTechnologyDay);
    }


    @Override
    public TechnologyDayResponse patchDaySplit(Long id, Long technologyId, TechnologyDayPatchRequest request) {

        log.info(
                "Patching technology day split. technologyDayId={}, technologyId={}",
                id,
                technologyId
        );

        Technology technology = technologyRepository.findById(technologyId)
                .orElseThrow(() -> {

                    log.warn(
                            "Technology day split patch failed. " +
                                    "Technology not found. technologyId={}",
                            technologyId
                    );

                    return new IncorrectTechnologyException(
                            "Technology id not found: " + technologyId
                    );
                });

        TechnologyDay technologyDay = technologyDayRepository.findById(id)
                .orElseThrow(() -> {

                    log.warn(
                            "Technology day split patch failed. " +
                                    "Technology day split not found. technologyDayId={}",
                            id
                    );

                    return new ResourceNotFoundException("Technology Day split not found");
                });


        mapper.patchEntity(request, technologyDay);

        technologyDay.setTechnology(technology);

        TechnologyDay updatedTechnologyDay = technologyDayRepository.save(technologyDay);

        log.info(
                "Technology day split patched successfully. " +
                        "technologyDayId={}, technologyId={}, dayNumber={}",
                updatedTechnologyDay.getId(),
                updatedTechnologyDay.getTechnology().getId(),
                updatedTechnologyDay.getDayNumber()
        );

        return mapper.toResponseDTO(updatedTechnologyDay);
    }


    public void deleteDaySplit(Long id) {

        log.info("Deleting technology day split. technologyDayId={}", id);

        TechnologyDay technologyDay = technologyDayRepository.findById(id)
                .orElseThrow(() -> {

                    log.warn(
                            "Technology day split deletion failed. " +
                                    "Technology day split not found. technologyDayId={}",
                            id
                    );

                    return new ResourceNotFoundException("Technology day split not found");
                });
        technologyDayRepository.delete(technologyDay);
    }


}
