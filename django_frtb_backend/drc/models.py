from django.db import models


class DrcCalibrationSummary(models.Model):
    issuer= models.CharField(max_length=200)
    beta1 = models.FloatField(default=0)
    beta2 = models.FloatField(default=0)
    residualVol = models.FloatField(default=0)
    factorCorr = models.FloatField(default=0)
    rSquare = models.FloatField(default=0)
    normalizationFactor = models.FloatField(default=0)
