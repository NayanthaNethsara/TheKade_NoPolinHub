"use client";

import { useState, useEffect } from "react";
import { useSession } from "next-auth/react";
import { useParams, useRouter } from "next/navigation";
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog";
import { Label } from "@/components/ui/label";
import { Textarea } from "@/components/ui/textarea";
import { Calendar } from "@/components/ui/calendar";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import {
  ArrowLeft,
  MapPin,
  Clock,
  Users,
  Star,
  CalendarIcon,
  FileText,
  CheckCircle,
} from "lucide-react";
import Link from "next/link";

interface Service {
  id: string;
  name: string;
  description: string;
  department: string;
  location: string;
  requirements: string[];
  estimated_duration: string;
  operating_hours: string;
  rating: number;
  total_bookings: number;
  is_active: boolean;
}

interface Booking {
  id: string;
  user_name: string;
  preferred_date: string;
  preferred_time: string;
  status: string;
  notes: string;
  created_at: string;
}

export default function ServiceDetailPage() {
  const { data: session } = useSession();
  const params = useParams();
  const router = useRouter();
  const [service, setService] = useState<Service | null>(null);
  const [bookings, setBookings] = useState<Booking[]>([]);
  const [loading, setLoading] = useState(true);
  const [isBookingDialogOpen, setIsBookingDialogOpen] = useState(false);
  const [selectedDate, setSelectedDate] = useState<Date>();
  const [bookingForm, setBookingForm] = useState({
    preferred_time: "",
    notes: "",
  });

  const isAdmin = session?.user?.role === "admin";

  useEffect(() => {
    if (params.id) {
      fetchService();
      if (isAdmin) {
        fetchBookings();
      }
    }
  }, [params.id, isAdmin]);

  const fetchService = async () => {
    try {
      const response = await fetch(`/api/services/${params.id}`);
      if (response.ok) {
        const data = await response.json();
        setService(data);
      }
    } catch (error) {
      console.error("Error fetching service:", error);
    } finally {
      setLoading(false);
    }
  };

  const fetchBookings = async () => {
    try {
      const response = await fetch(`/api/services/${params.id}/bookings`);
      if (response.ok) {
        const data = await response.json();
        setBookings(data);
      }
    } catch (error) {
      console.error("Error fetching bookings:", error);
    }
  };

  const handleBooking = async () => {
    if (!selectedDate) return;

    try {
      const response = await fetch(`/api/services/${params.id}/bookings`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          preferred_date: selectedDate.toISOString().split("T")[0],
          preferred_time: bookingForm.preferred_time,
          notes: bookingForm.notes,
        }),
      });

      if (response.ok) {
        setIsBookingDialogOpen(false);
        setBookingForm({ preferred_time: "", notes: "" });
        setSelectedDate(undefined);
        // Show success message or redirect
        alert("Booking request submitted successfully!");
      }
    } catch (error) {
      console.error("Error creating booking:", error);
    }
  };

  if (loading) {
    return (
      <div className="min-h-screen bg-gradient-to-br from-gray-50 to-gray-100 dark:from-gray-950 dark:to-gray-900 p-6">
        <div className="max-w-4xl mx-auto">
          <div className="h-96 bg-white/20 dark:bg-gray-800/20 backdrop-blur-sm rounded-xl animate-pulse" />
        </div>
      </div>
    );
  }

  if (!service) {
    return (
      <div className="min-h-screen bg-gradient-to-br from-gray-50 to-gray-100 dark:from-gray-950 dark:to-gray-900 p-6">
        <div className="max-w-4xl mx-auto text-center py-12">
          <p className="text-gray-500 dark:text-gray-400">Service not found.</p>
          <Link href="/services">
            <Button className="mt-4">Back to Services</Button>
          </Link>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-gray-50 to-gray-100 dark:from-gray-950 dark:to-gray-900 p-6">
      <div className="max-w-4xl mx-auto space-y-6">
        <div className="flex items-center gap-4">
          <Link href="/services">
            <Button
              variant="outline"
              size="sm"
              className="bg-white/50 dark:bg-gray-800/50 backdrop-blur-sm border-gray-200/20 dark:border-gray-700/20"
            >
              <ArrowLeft className="w-4 h-4 mr-2" />
              Back to Services
            </Button>
          </Link>
          <div>
            <h1 className="text-3xl font-bold text-gray-900 dark:text-white">
              {service.name}
            </h1>
            <p className="text-gray-600 dark:text-gray-400">
              {service.department} Department
            </p>
          </div>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
          <div className="lg:col-span-2 space-y-6">
            <Card className="bg-white/20 dark:bg-gray-800/20 backdrop-blur-sm border-gray-200/20 dark:border-gray-700/20 shadow-lg">
              <CardHeader>
                <div className="flex justify-between items-start">
                  <div>
                    <CardTitle className="text-xl text-gray-900 dark:text-white">
                      Service Information
                    </CardTitle>
                    <CardDescription className="text-gray-600 dark:text-gray-400 mt-2">
                      {service.description}
                    </CardDescription>
                  </div>
                  <Badge
                    variant={service.is_active ? "default" : "secondary"}
                    className={
                      service.is_active
                        ? "bg-gray-900 text-white"
                        : "bg-gray-500 text-white"
                    }
                  >
                    {service.is_active ? "Active" : "Inactive"}
                  </Badge>
                </div>
              </CardHeader>
              <CardContent className="space-y-4">
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                  <div className="flex items-center text-gray-600 dark:text-gray-400">
                    <MapPin className="w-5 h-5 mr-3" />
                    <div>
                      <p className="font-medium">Location</p>
                      <p className="text-sm">{service.location}</p>
                    </div>
                  </div>
                  <div className="flex items-center text-gray-600 dark:text-gray-400">
                    <Clock className="w-5 h-5 mr-3" />
                    <div>
                      <p className="font-medium">Duration</p>
                      <p className="text-sm">{service.estimated_duration}</p>
                    </div>
                  </div>
                  <div className="flex items-center text-gray-600 dark:text-gray-400">
                    <Users className="w-5 h-5 mr-3" />
                    <div>
                      <p className="font-medium">Total Bookings</p>
                      <p className="text-sm">{service.total_bookings}</p>
                    </div>
                  </div>
                  <div className="flex items-center text-gray-600 dark:text-gray-400">
                    <Star className="w-5 h-5 mr-3 fill-current" />
                    <div>
                      <p className="font-medium">Rating</p>
                      <p className="text-sm">{service.rating}/5.0</p>
                    </div>
                  </div>
                </div>

                <div>
                  <h3 className="font-medium text-gray-900 dark:text-white mb-2">
                    Operating Hours
                  </h3>
                  <p className="text-gray-600 dark:text-gray-400">
                    {service.operating_hours}
                  </p>
                </div>

                <div>
                  <h3 className="font-medium text-gray-900 dark:text-white mb-2">
                    Requirements
                  </h3>
                  <div className="flex flex-wrap gap-2">
                    {service.requirements.map((req, index) => (
                      <Badge key={index} variant="outline" className="text-xs">
                        <FileText className="w-3 h-3 mr-1" />
                        {req}
                      </Badge>
                    ))}
                  </div>
                </div>
              </CardContent>
            </Card>

            {isAdmin && (
              <Card className="bg-white/20 dark:bg-gray-800/20 backdrop-blur-sm border-gray-200/20 dark:border-gray-700/20 shadow-lg">
                <CardHeader>
                  <CardTitle className="text-xl text-gray-900 dark:text-white">
                    Recent Bookings
                  </CardTitle>
                  <CardDescription>
                    Latest booking requests for this service
                  </CardDescription>
                </CardHeader>
                <CardContent>
                  <div className="space-y-3">
                    {bookings.length > 0 ? (
                      bookings.slice(0, 5).map((booking) => (
                        <div
                          key={booking.id}
                          className="flex justify-between items-center p-3 bg-white/30 dark:bg-gray-700/30 rounded-lg"
                        >
                          <div>
                            <p className="font-medium text-gray-900 dark:text-white">
                              {booking.user_name}
                            </p>
                            <p className="text-sm text-gray-600 dark:text-gray-400">
                              {booking.preferred_date} at{" "}
                              {booking.preferred_time}
                            </p>
                          </div>
                          <Badge
                            variant={
                              booking.status === "confirmed"
                                ? "default"
                                : "secondary"
                            }
                            className={
                              booking.status === "confirmed"
                                ? "bg-green-600 text-white"
                                : "bg-yellow-600 text-white"
                            }
                          >
                            {booking.status}
                          </Badge>
                        </div>
                      ))
                    ) : (
                      <p className="text-gray-500 dark:text-gray-400 text-center py-4">
                        No bookings yet
                      </p>
                    )}
                  </div>
                </CardContent>
              </Card>
            )}
          </div>

          <div className="space-y-6">
            {!isAdmin && service.is_active && (
              <Card className="bg-white/20 dark:bg-gray-800/20 backdrop-blur-sm border-gray-200/20 dark:border-gray-700/20 shadow-lg">
                <CardHeader>
                  <CardTitle className="text-lg text-gray-900 dark:text-white">
                    Book Appointment
                  </CardTitle>
                  <CardDescription>
                    Schedule your visit to this service
                  </CardDescription>
                </CardHeader>
                <CardContent>
                  <Dialog
                    open={isBookingDialogOpen}
                    onOpenChange={setIsBookingDialogOpen}
                  >
                    <DialogTrigger asChild>
                      <Button className="w-full bg-gray-900 hover:bg-gray-800 text-white">
                        <CalendarIcon className="w-4 h-4 mr-2" />
                        Book Now
                      </Button>
                    </DialogTrigger>
                    <DialogContent className="max-w-md bg-white/95 dark:bg-gray-900/95 backdrop-blur-sm border-gray-200/20 dark:border-gray-700/20">
                      <DialogHeader>
                        <DialogTitle>Book Appointment</DialogTitle>
                        <DialogDescription>
                          Choose your preferred date and time
                        </DialogDescription>
                      </DialogHeader>
                      <div className="space-y-4">
                        <div>
                          <Label>Preferred Date</Label>
                          <Calendar
                            mode="single"
                            selected={selectedDate}
                            onSelect={setSelectedDate}
                            disabled={(date) => date < new Date()}
                            className="rounded-md border bg-white/50 dark:bg-gray-800/50"
                          />
                        </div>
                        <div>
                          <Label htmlFor="time">Preferred Time</Label>
                          <Select
                            value={bookingForm.preferred_time}
                            onValueChange={(value) =>
                              setBookingForm({
                                ...bookingForm,
                                preferred_time: value,
                              })
                            }
                          >
                            <SelectTrigger className="bg-white/50 dark:bg-gray-800/50 border-gray-200/20 dark:border-gray-700/20">
                              <SelectValue placeholder="Select time" />
                            </SelectTrigger>
                            <SelectContent>
                              <SelectItem value="09:00">9:00 AM</SelectItem>
                              <SelectItem value="10:00">10:00 AM</SelectItem>
                              <SelectItem value="11:00">11:00 AM</SelectItem>
                              <SelectItem value="14:00">2:00 PM</SelectItem>
                              <SelectItem value="15:00">3:00 PM</SelectItem>
                              <SelectItem value="16:00">4:00 PM</SelectItem>
                            </SelectContent>
                          </Select>
                        </div>
                        <div>
                          <Label htmlFor="notes">Additional Notes</Label>
                          <Textarea
                            id="notes"
                            value={bookingForm.notes}
                            onChange={(e) =>
                              setBookingForm({
                                ...bookingForm,
                                notes: e.target.value,
                              })
                            }
                            placeholder="Any special requirements or notes..."
                            className="bg-white/50 dark:bg-gray-800/50 border-gray-200/20 dark:border-gray-700/20"
                          />
                        </div>
                      </div>
                      <div className="flex justify-end space-x-2 pt-4">
                        <Button
                          variant="outline"
                          onClick={() => setIsBookingDialogOpen(false)}
                        >
                          Cancel
                        </Button>
                        <Button
                          onClick={handleBooking}
                          disabled={
                            !selectedDate || !bookingForm.preferred_time
                          }
                          className="bg-gray-900 hover:bg-gray-800 text-white"
                        >
                          <CheckCircle className="w-4 h-4 mr-2" />
                          Confirm Booking
                        </Button>
                      </div>
                    </DialogContent>
                  </Dialog>
                </CardContent>
              </Card>
            )}

            {!service.is_active && (
              <Card className="bg-red-50/20 dark:bg-red-900/20 backdrop-blur-sm border-red-200/20 dark:border-red-700/20 shadow-lg">
                <CardContent className="pt-6">
                  <p className="text-red-600 dark:text-red-400 text-center">
                    This service is currently unavailable for booking.
                  </p>
                </CardContent>
              </Card>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}
