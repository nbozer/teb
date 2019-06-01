

Log oluşturmak için LogCreatorService OSGi bundle'ı oluşturuldu. 
LogCreatorService ile KafkaProducerService OSGi bundle'ları aralarındaki api LogCreatorAPI'dir.
LogCreatorService random olarak dökümanda belirtilen şekilde log oluşturur ve bunu bir text dosyasına yazar. Aynı bundle bunu text dosyasından okur ve Kafka'ya publis olabilmesi için api yardımıyla KafkaProducerService'ye bu log bilgisini gönderir.
KafkaProducerService ise log'u kafkaya "teb" topic'i ile publish eder.

KafkaConsumerService ise bu topic'ten okuma yapar ve her topic basıldığında topicteki datayı consume eder. Alınan datayı gerekli parse işlemlerinden sonra bir text dosyasına yazar. 

Consumer'ın yazdığı text dosyasını, web projesinden okuyup browser'da göstermek amaçlanmıştır.




