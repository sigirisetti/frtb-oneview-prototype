from rest_framework import serializers
from drc.models import DrcCalibrationSummary


class DrcModelSerializer(serializers.ModelSerializer):
    class Meta:
        model = DrcCalibrationSummary
        fields = ('__all__')
