from django.shortcuts import render, HttpResponseRedirect
from django.http import HttpResponse, JsonResponse

from drc.models import DrcCalibrationSummary
from drc.serializers import DrcModelSerializer

from django.views.decorators.csrf import csrf_exempt


@csrf_exempt
def get_drc_calib_summary(request):
    print("Executing get_drc_calib_summary")
    data = DrcCalibrationSummary.objects.all()
    if request.method == 'GET':
        serializer = DrcModelSerializer(data, many=True)
        r = JsonResponse(serializer.data, safe=False)
        print(r)
        return r
