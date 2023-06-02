from django.http import JsonResponse
from drc.models import DrcCalibrationSummary
from drc.serializers import DrcModelSerializer


def get_drc_calib_summary(request):
    print("Executing get_drc_calib_summary")
    data = DrcCalibrationSummary.objects.all()
    if request.method == 'GET':
        serializer = DrcModelSerializer(data, many=True)
        r = JsonResponse(serializer.data, safe=False)
        # print(serializers.serialize('json', data))
        return r
