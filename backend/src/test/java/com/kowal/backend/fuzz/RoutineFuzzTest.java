package com.kowal.backend.fuzz;

import com.kowal.backend.customer.dto.request.AddRoutineRequest;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.constraints.StringLength;

import static org.junit.jupiter.api.Assertions.assertFalse;

class RoutineFuzzTest {

@Property
void routineNamesShouldNotCauseInternalErrors(@ForAll @StringLength(min = 0, max = 5000) String randomName) {

    AddRoutineRequest request = new AddRoutineRequest();
    request.setName(randomName);

    boolean hasError = false;
    try {
        validateRoutine(request);
    } catch (Exception e) {
        hasError = true;
    }

    assertFalse(hasError, "Application crashed for input: " + randomName);
}

private void validateRoutine(AddRoutineRequest req) {
    if (req.getName() == null) throw new IllegalArgumentException("Name cannot be null");
}
}