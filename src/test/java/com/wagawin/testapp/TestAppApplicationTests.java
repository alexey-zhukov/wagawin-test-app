package com.wagawin.testapp;

import com.wagawin.testapp.entity.*;
import com.wagawin.testapp.repository.ChildRepository;
import com.wagawin.testapp.repository.HouseRepository;
import com.wagawin.testapp.repository.MealRepository;
import com.wagawin.testapp.repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.*;

@SpringBootTest
@Slf4j class TestAppApplicationTests {
    @Autowired private HouseRepository houseRepository;
    @Autowired private MealRepository mealRepository;
    @Autowired private PersonRepository personRepository;
    @Autowired private ChildRepository childRepository;

    @Test void createHouses() {
        Random random = new Random();
        HouseType[] types = HouseType.values();
        int totalSize = 10_000;
        int batchSize = 1_000;
        for (int nI = 0; nI < totalSize;) {
            List<House> batchHouses = new ArrayList<>(batchSize);
            List<Person> batchPersons = new ArrayList<>(batchSize);
            for (int nJ = 0; nJ < batchSize; ++nJ) {
                House house = new House()
                        .setAddress("Address " + nI + nJ)
                        .setType(types[random.nextInt(types.length)])
                        .setZipCode(String.format("%06d", nI + nJ));
                batchHouses.add(house);
                batchPersons.add(new Person()
                        .setHouse(house)
                        .setAge(random.nextInt(10) + 20)
                        .setName("Person " + (nI + nJ)));
            }
            houseRepository.saveAll(batchHouses);
            personRepository.saveAll(batchPersons);
            nI += batchSize;
        }
    }

    @Test void createMeals() {
        Random random = new Random();
        int totalSize = 1_000;
        List<Meal> batch = new ArrayList<>(totalSize);
        for (int nI = 0; nI < totalSize; ++nI) {
            batch.add(new Meal()
                    .setName("Meal " + nI)
                    .setInvented(new Date(random.nextInt() * 1000L))
            );
        }
        mealRepository.saveAll(batch);
    }

    @Transactional @Rollback(false)
    @Test void createChildren() {
        Integer personMinId = personRepository.getMinId();
        Integer personMaxId = personRepository.getMaxId();

        List<Meal> meals = mealRepository.findAll();

        Random random = new Random();
        int totalSize = 70_000;
        int batchSize = 1_000;
        for (int nI = 0; nI < totalSize; ) {
            List<Child> batch = new ArrayList<>(batchSize);
            Set<Integer> addedMeals = new HashSet<>(3);
            for (int nJ = 0; nJ < batchSize; ++nJ) {
                // [!] to increase speed of TEST data generation we will assume we don't have empty spaces in identifiers
                /*Person parent;
                do {
                    // get random parent from db:
                    parent = this.personRepository.findById(random.nextInt(personMaxId - personMinId) + personMinId).orElse(null);
                } while (parent == null);*/


                Child nextChild;
                switch (random.nextInt(2)) {
                    case 0:
                        nextChild = new Son()
                                .setBicycleColor(Color.values()[random.nextInt(Color.values().length)].toString())
                                .setName("Son " + (nI + nJ));
                        break;
                    case 1:
                    default:
                        nextChild = new Daughter()
                                .setHairColor(Color.values()[random.nextInt(Color.values().length)].toString())
                                .setName("Daughter " + (nI + nJ));
                        break;
                }

                // collect collection of meals
                int mealsCnt = random.nextInt(3);
                addedMeals.clear();
                for (int nM = 0; nM < (mealsCnt + 1); ++nM) {
                    Meal meal = meals.get(random.nextInt(meals.size()));
                    // avoid adding duplicated meals:
                    if (addedMeals.contains(meal.getId())) {
                        // if got already added -- repeat random selection:
                        nM--;
                    } else {
                        nextChild.addMeal(meal, nM);
                        addedMeals.add(meal.getId());
                    }
                }

                batch.add(nextChild
                        .setAge(random.nextInt(10))
                        .setParentId(random.nextInt(personMaxId - personMinId) + personMinId)
                );
            }
            childRepository.saveAll(batch);
            nI += batchSize;
            log.info("Inserted {}", nI);
        }
    }
}
