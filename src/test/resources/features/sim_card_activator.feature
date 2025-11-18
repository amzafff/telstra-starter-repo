Feature: SIM Card Activation

  Scenario: Successful SIM card activation
    Given the microservice is running
    When I submit an activation request for ICCID "1255789453849037777" and customer email "user@example.com"
    Then the activation is successful
    And querying activation record with id 1 returns success status

  Scenario: Failed SIM card activation
    Given the microservice is running
    When I submit an activation request for ICCID "8944500102198304826" and customer email "user@example.com"
    Then the activation fails
    And querying activation record with id 2 returns failure status
