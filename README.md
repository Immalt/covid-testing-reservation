# covid-testing-reservation

- API for FE
   - should provide data for FE
- static asset server for FE
   - webpage, style files etc.
- service for time slot managmenet
   - list time slots
   - book concrete time slot
   - cancel reserved time slot
- service for personal data (přihlášky na  testování + výsledky)
   - retrive registration data
   - create registration data
   - add results for registration
- service for writing the results
   - results will be send to this service and forwarded to notification service
   - results will be send to this service and forwarded to personal data service
- service for notification (email+SMS)
   - sending results
   - sending after registration with link to be able to cancel the reserved time
