package com.wagawin.testapp.service;

import com.wagawin.testapp.dto.ColorDto;
import com.wagawin.testapp.entity.Child;
import com.wagawin.testapp.entity.Daughter;
import com.wagawin.testapp.entity.Son;
import com.wagawin.testapp.repository.ChildRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@CacheConfig(cacheNames = "color-cache")
@Service @Slf4j
public class ColorService {
    private ChildRepository childRepository;
    @Autowired private ColorService colorService;

    public ColorService(ChildRepository childRepository) {
        this.childRepository = childRepository;
    }

    @Cacheable(key = "#id")
    public ColorDto get(Integer id) {
        Optional<Child> byId = this.childRepository.findById(id);
        return byId.map(ColorDto::new).orElse(null);
    }

    public ColorDto save(Integer id, String color) {
        Optional<Child> byId = this.childRepository.findById(id);
        if (byId.isPresent()) {
            Child child = byId.get();
            if (child instanceof Son)
                ((Son) child).setBicycleColor(color);
            else if (child instanceof Daughter)
                ((Daughter) child).setHairColor(color);
            this.childRepository.save(child);
            return this.colorService.put(new ColorDto(child));
        } else
            return null;
    }

    @CachePut(key = "#color.id")
    public ColorDto put(ColorDto color) {
        return color;
    }
}