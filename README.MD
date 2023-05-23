## Task Board App

This is a simple task board application that allows you to manage tasks and boards through RESTful APIs.
API Endpoints
### Task Controller

    PUT /tasks/{id}: Overwrites a task with the specified ID. The request body should contain a valid TaskDto object. Returns the updated task as a TaskDto.

    PATCH /tasks/{id}: Updates a task with the specified ID. The request body should contain a TaskDto object with the fields to be updated. Returns the updated task as a TaskDto.

    DELETE /tasks/{id}: Deletes a task with the specified ID.

### Board Controller

    GET /boards: Retrieves all boards. Returns a list of BoardDto objects.

    POST /boards: Creates a new board. The request body should contain a valid BoardDto object. Returns the created board as a BoardDto.

    GET /boards/{id}: Retrieves a board with the specified ID. Returns the board as a BoardDto with associated tasks.

    DELETE /boards/{id}: Deletes a board with the specified ID.

    POST /boards/{id}/tasks: Creates a new task for the board with the specified ID. The request body should contain a valid TaskDto object. Returns the created task as a TaskDto.

### Webhooks Controller
    
    POST /webhooks/user-deleted: Handles the "user-deleted" webhook by extracting the user ID from the payload and invoking the deleteTasks method of the TaskAsyncDeletionService. Returns a 200 OK response to acknowledge the successful processing of the webhook.

