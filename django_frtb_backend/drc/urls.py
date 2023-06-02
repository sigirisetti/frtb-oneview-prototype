from django.urls import path

from . import views

urlpatterns = [
    path("get_drc_calib_summary", views.get_drc_calib_summary, name="get_drc_calib_summary"),
]
