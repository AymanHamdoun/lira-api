## Repository Getters 
The following functions are available for any repository that extends ```lira.ahamdoun.repositories.BaseRepository```

``` kotlin
    val allModels = repo.getAll()
    
    val allModelsWithCondition = repo.getAll(mapOf(
        "column1" to "value1",
        "column2" to "value2",
    ))
```