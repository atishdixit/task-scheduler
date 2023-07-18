## Add new job in task scheduler
### Request
```
POST http://localhost:8080/job

{
  "description": "job minutes later",
  "time": 1,
  "scheduleType": "MINUTES",
  "task": "CLEAN_UP"
}

or 
{
  "description": "job minutes later",
  "time": 1,
  "scheduleType": "MINUTES",
  "task": "MIGRATE_DATA"
}

scheduleType: [SECONDS,  MINUTES, EXACT_TIME]
task: [    EMAIL_NOTIFICATION, SMS_NOTIFICATION, PUSH_NOTIFICATION, CLEAN_UP, READ_SERVER_DATA, MIGRATE_DATA]
NOTE: Only two type of task working CLEAN_UP, MIGRATE_DATA
```
### Response

```
{
    "id": "9b123658-3e95-4cfc-9b92-ca09ef56df5f",
    "createdAt": "2023-07-18T09:29:37.886352",
    "exactTime": null,
    "description": "Job minutes later",
    "scheduleType": "MINUTES",
    "task": "CLEAN_UP",
    "executedAt": null,
    "latency": 0,
    "time": 1,
    "executed": false,
    "updated": false
}
```

## To Add new Task implementations.  
1. Add new class and implements Task interface.
2. Implement execute method.
3. Add task name from Name-enum. 
3. Add entry on task factory(TaskFactory) class.
